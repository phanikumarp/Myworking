package n42.tools.generate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import n42.common.Charsets;
import n42.config.model.FieldType;
import n42.config.model.ModelContainer;
import n42.config.model.ModelDefinition;
import n42.services.config.XStreamConfigManager;
import n42.services.model.info.DomainModelInfoService;
import n42.services.model.info.InfoService;
import n42.services.model.info.ModelDefinitionException;
import n42.services.model.persist.LazyIdGenerator;
import n42.services.serialize.IFieldDefinition;
import n42.services.serialize.IModelDefinition;
import n42.services.utils.ExceptionUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicHibernateModelGenerator { 

	private static final Logger LOG = LoggerFactory.getLogger(DynamicHibernateModelGenerator.class);

	private static final Set<String> METADATA_PROPERTIES = new HashSet<>();

	/**
	 * Maps Java class to a hibernate type.
	 */
	private static final Map<FieldType, String> HIBERNATE_TYPE_MAP;

	static {
		METADATA_PROPERTIES.add("n42id");
//		METADATA_PROPERTIES.add("n42version");
		METADATA_PROPERTIES.add("n42createddate");
		METADATA_PROPERTIES.add("n42lastupdateddate");
		METADATA_PROPERTIES.add("n42hash");
		METADATA_PROPERTIES.add("active");

		//So far we need only one special type mapping: IAdaptable -> text (for "json" type fields)
		HIBERNATE_TYPE_MAP = new HashMap<>();
		HIBERNATE_TYPE_MAP.put(FieldType.TEXT, "text");
//		HIBERNATE_TYPE_MAP.put(FieldType.JSON, "text");

	}

	private InfoService infoService;

	private String idGenerator = LazyIdGenerator.class.getName();  //"n42.services.model.persist.LazyIdGenerator"

	public void setIdGenerator(String idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}

	public void generateMappings(String outputDirectory) throws IOException, ModelDefinitionException {

		try (PrintWriter out = new PrintWriter(createFileWriter(outputDirectory))) {
			out.println("<?xml version=\"1.0\"?>");
			out.println("<!DOCTYPE hibernate-mapping SYSTEM");
			out.println("\"classpath:///hibernate-mapping-3.0.dtd\">");
			out.println("<hibernate-mapping default-cascade=\"all-delete-orphan\">");
			for (IModelDefinition def: getModelDefinitions()) {
				writeOutClass(out, def);
			}

			out.println("</hibernate-mapping>");
			out.flush();
		}
	}

	protected Iterable<IModelDefinition> getModelDefinitions() {

		final ModelContainer container = ((DomainModelInfoService) infoService).mergeContainers();  // TODO: work off of interface if possible
		return new Iterable<IModelDefinition>() {

			@Override
			public Iterator<IModelDefinition> iterator() {

				final Iterator<ModelDefinition> delegate = container.getDefinitions().iterator();
				return new Iterator<IModelDefinition>() {
					@Override 
					public boolean hasNext() { return delegate.hasNext(); }
					@Override 
					public IModelDefinition next() { return delegate.next(); }
					@Override 
					public void remove() { delegate.remove(); }
				};
			}
		};
	}

	protected Writer createFileWriter(String outputDirectory) throws IOException {
		File outputFile = new File(outputDirectory);
		File parentFile = outputFile.getParentFile();
		if (parentFile == null) {
			throw new IllegalArgumentException("Parent file is null");
		}

		if (!parentFile.exists() && !parentFile.mkdirs()) {
			throw new IllegalStateException("Unable to create directory: " + parentFile);
		}

		return new OutputStreamWriter(new FileOutputStream(outputFile), Charsets.UTF8);
	}

	private void writeOutClass(PrintWriter out, IModelDefinition def) {

		if (def.getInherits() != null) {
			out.print("<subclass name=\"");
			out.print(def.getQualifiedName());
			out.print("\" extends=\"");
			out.print(def.getInherits());
			out.print("\" discriminator-value=\"");
			out.print(def.getPackageName() + def.getName());
			out.println("\">");
		} else {

			out.print("<class name=\"");
			out.print(def.getQualifiedName());
			out.print("\" table=\"");
			out.print(tableName(def.getName().toLowerCase(Locale.ENGLISH)));
			out.println("\">");

			out.println("  <meta attribute=\"extends\" inherit=\"false\">n42.model.domain.DomainBase</meta>");
			if (def.getCache() != null) {
				out.println("<cache usage=\""+def.getCache()+"\"/>");
			}
			out.println("  <id name=\"n42Id\" type=\"integer\" column=\"N42_ID\">");
			out.print("    <generator class=\"");
			out.print(idGenerator);
			out.println("\"/>");
			out.println("  </id>");
			out.println("  <discriminator column=\"TYPE\" type=\"string\"/>");
//			out.println("  <version name=\"n42Version\" column=\"N42_VERSION\"/>");
			out.println("  <property name=\"n42CreatedDate\" type=\"timestamp\"/>");
			out.println("  <property name=\"n42LastUpdatedDate\" type=\"timestamp\"/>");
			out.println("  <property name=\"n42Hash\" type=\"string\"/>");
			out.println("  <property name=\"active\" type=\"java.lang.Boolean\"/>");

		}

		
		for (IFieldDefinition fd: def.getFields()) {
			writeOutField(out, fd, def.getName());
		}

		//these need to go last.
		for (IFieldDefinition fd: def.getFields()) {
			writeOutJoinField(out, fd, def.getName());
		}

		if (def.getInherits() != null) {
			out.println("</subclass>");
		} else {
			out.println("</class>");
		}
	}

	private void writeOutField(PrintWriter outputStream, IFieldDefinition def, String parentModelName) {
		if (FieldType.isBasicType(def.getType())) {
			if (def.isList()) {
				writeOutSimpleListField(outputStream, def, parentModelName);
			} else {
				writeOutSimpleField(outputStream, def);
			}
		} else {
			IModelDefinition fieldType = infoService.getDefinitionByPartialName(def.getType());
			if (fieldType == null) {
				LOG.warn("writeOutField: No model found for: " + def.getType());
				return;
			}
			if (def.isList()) {
				writeOutComplexListField(outputStream, def, parentModelName, fieldType);
			} else if (def.getInverse() != null) {
				writeOutComplexInverseField(outputStream, def, fieldType);
			} else {
				writeOutComplexField(outputStream, def, fieldType);
			}
		}
	}

	private void writeOutJoinField(PrintWriter outputStream, IFieldDefinition def, String parentModelName) {

		if (def.isLinkType() && !FieldType.isBasicType(def.getType()) && !def.isList() && def.getInverse() == null) {

			IModelDefinition md = infoService.getDefinitionByPartialName(def.getType());
			if (md == null) {
				LOG.warn("writeOutJoinField: No model found for: " + def.getType());
				return;
			}

			List<IFieldDefinition> fields = md.getFields();
			String property = null;
			for (IFieldDefinition fd: fields) {
				if (def.getName().equals(fd.getInverse())) {
					property = fd.getName();
					break;
				}
			}

			if (property == null) {

				outputStream.print("  <join table=\"");
				outputStream.print(tableName(parentModelName.toLowerCase(Locale.ENGLISH)));
				outputStream.print('_');
				outputStream.print(def.getType().toLowerCase(Locale.ENGLISH));
				outputStream.println("\" optional=\"true\">");

				outputStream.print("    <key column=\"");
				outputStream.print(parentModelName.toLowerCase(Locale.ENGLISH));
				outputStream.println("\"/>");
				outputStream.print("    <many-to-one class=\"");
				outputStream.print(md.getPackageName());
				outputStream.print(def.getType());
				outputStream.print("\" column=\"");
				outputStream.print(def.getName().toLowerCase(Locale.ENGLISH));
				outputStream.print("\" name=\"");
				outputStream.print(def.getName());
				outputStream.println("\" not-null=\"true\" cascade=\"save-update\" />");

				outputStream.println("  </join>");

			}
		}
	}

	private void writeOutSimpleField(PrintWriter outputStream, IFieldDefinition def) {
		if (METADATA_PROPERTIES.contains(def.getName().toLowerCase(Locale.ENGLISH))) {
			return;
		}

		outputStream.print("  <property name=\"");
		outputStream.print(def.getName());
		outputStream.print("\" type=\"");
		outputStream.print(toHibernateType(def));
		if (def.isUnique()) {
			outputStream.print("\" unique=\"");
			outputStream.print(def.isUnique());
		}
		if (def.isNotNullable()) {
			outputStream.print("\" not-null=\"");
			outputStream.print(def.isNotNullable());
		}
		outputStream.println("\"/>");
	}

	private void writeOutComplexInverseField(PrintWriter outputStream, IFieldDefinition def, IModelDefinition fieldType) {
		outputStream.print("  <many-to-one name=\"");
		outputStream.print(def.getName());
		outputStream.print("\" class=\"");
		outputStream.print(fieldType.getPackageName());
		outputStream.print(def.getType());
		outputStream.print("\" column=\"");
		outputStream.print(def.getInverse());
		outputStream.print('_');
		outputStream.print(def.getType());

		//opposite of one-to-one is different

		IFieldDefinition fd = fieldType.lookupField(def.getInverse(), false);
		if (fd == null) {
			throw new IllegalArgumentException("Error finding inverse relationship: " + def.getInverse());
		}

		if (def.getFetch() != null) {
			outputStream.print("\" lazy=\"false\" fetch=\"");
			outputStream.print(def.getFetch());
		}
		if (fd.isList()) {
			outputStream.println("\" insert=\"false\" update=\"false\"/>");
		} else {
			outputStream.println("\" cascade=\"all\"/>");
		}
	}

	private void writeOutComplexField(PrintWriter outputStream, IFieldDefinition def, IModelDefinition fieldType) {

		List<IFieldDefinition> fields = fieldType.getFields();
		String property = null;
		for (IFieldDefinition fd: fields) {
			if (def.getName().equals(fd.getInverse())) {
				property = fd.getName();
				break;
			}
		}

		if (property == null) {
			if (!def.isLinkType()) {
				outputStream.print("  <many-to-one name=\"");
				outputStream.print(def.getName());
				outputStream.print("\" class=\"");
				outputStream.print(fieldType.getPackageName());
				outputStream.print(def.getType());
				outputStream.print("\" column=\"");
				outputStream.print(def.getName());
				outputStream.print('_');
				outputStream.print(def.getType());
				if (def.getFetch() != null) {
					outputStream.print("\" lazy=\"false\" fetch=\"");
					outputStream.print(def.getFetch());
				}
				outputStream.println("\" cascade=\"all\"/>");
			}
		} else {
			outputStream.print("  <one-to-one name=\"");
			outputStream.print(def.getName());
			outputStream.print("\" class=\"");
			outputStream.print(fieldType.getPackageName());
			outputStream.print(def.getType());
			outputStream.print("\" property-ref=\"");
			outputStream.print(property);
			outputStream.print("\"");
			if (def.isLinkType()) {
				outputStream.print(" cascade=\"save-update\" foreign-key=\"none\"");
			}
			outputStream.println("/>");
		}

	}

	private void writeOutComplexListField(PrintWriter outputStream, IFieldDefinition def, String parentModelName, IModelDefinition fieldType) {

		if (def.isLinkType()) {
			//we don't want to delete, and we also might have many in the
			//same list
			outputStream.print("  <list name=\"");
			outputStream.print(def.getName());
			outputStream.print("\" table=\"");
			outputStream.print(tableName(parentModelName.toLowerCase(Locale.ENGLISH)));
			outputStream.print('_');
			outputStream.print(def.getName().toLowerCase(Locale.ENGLISH));
			if (def.getFetch() != null) {
				outputStream.print("\" lazy=\"false\" fetch=\"");
				outputStream.print(def.getFetch());
			}
			outputStream.println("\" cascade=\"save-update\">");
		} else {
			outputStream.print("  <list name=\"");
			outputStream.print(def.getName());
			outputStream.print("\" inverse=\"false\"");
			if (def.getFetch() != null) {
				outputStream.print(" lazy=\"false\" fetch=\"");
				outputStream.print(def.getFetch());
				outputStream.print("\"");
			}
			outputStream.println(" cascade=\"all\">");
		}

		outputStream.print("    <key column=\"");
		outputStream.print(def.getName());
		outputStream.print('_');
		outputStream.print(parentModelName);
		outputStream.println("\"/>");
		outputStream.println("    <list-index column=\"SORT_ORDER\"/>");

		if (def.isLinkType()) {
			outputStream.print("    <many-to-many column=\"");
			outputStream.print(def.getType());
			outputStream.print("\" class=\"");
			outputStream.print(fieldType.getPackageName());
			outputStream.print(def.getType());
		} else {
			outputStream.print("    <one-to-many class=\"");
			outputStream.print(fieldType.getPackageName());
			outputStream.print(def.getType());
		}

		outputStream.println("\"/>");
		outputStream.println("  </list>");
	}

	private void writeOutSimpleListField(PrintWriter outputStream, IFieldDefinition def, String parentModelName) {

		outputStream.print("  <list name=\"");
		outputStream.print(def.getName());
		outputStream.print("\" table=\"");
		outputStream.print(tableName(parentModelName.toLowerCase(Locale.ENGLISH)));
		outputStream.print("_");
		outputStream.print(def.getName().toLowerCase(Locale.ENGLISH));
		outputStream.print("\"");
		if (def.getFetch() != null) {
			outputStream.print(" lazy=\"false\" fetch=\"");
			outputStream.print(def.getFetch());
			outputStream.println("\">");
		}
		else {
			outputStream.println(">");
		}
		
		
		outputStream.println("    <key column=\"PARENT_ID\"/>");
		outputStream.println("    <list-index column=\"SORT_ORDER\"/>");
		outputStream.print("    <element type=\"");
		outputStream.print(toHibernateType(def));
		outputStream.println("\" column=\"VALUE\"/>");
		outputStream.println("  </list>");
	}

	private static String tableName(String name) {

		String tableName = name;
		if (tableName.startsWith("Std")) {
			tableName = tableName.substring(tableName.indexOf("Std"));
			tableName = Character.toLowerCase(tableName.charAt(0)) + tableName.substring(1);
		}
		return tableName;
	}

	/**
	 * Takes into account the nullability of the field when mapping to Hibernate.
	 * @param def
	 * @return
	 */
	private String toHibernateType(IFieldDefinition def) {
		FieldType fieldType = FieldType.fromTypeName(def.getType());
		Class<?> javaType = fieldType.getJavaType(def.isNotNullable());

		String hibernateType = (HIBERNATE_TYPE_MAP.containsKey(fieldType)? HIBERNATE_TYPE_MAP.get(fieldType) : javaType.getCanonicalName());

		return hibernateType;
	}

	/** 
	 * @param args
	 */
	public static void main(String[] args) {

		ConsoleAppender appender = new ConsoleAppender(new PatternLayout("[%p] %d{HH:mm:ss.SSS} %t [%c] - %m%n"), ConsoleAppender.SYSTEM_OUT);
		appender.setThreshold(Level.INFO);
		org.apache.log4j.Logger.getRootLogger().addAppender(appender);
		if (args.length < 3) {
			return;
		}
		String outPath = args[0];
		String idGen = args[1];
		String categoriesCsv = args[2];

		XStreamConfigManager manager = new XStreamConfigManager();
		manager.registerClasses(DomainModelInfoService.MODEL_CONFIG_CLASSES);
		try {
			DomainModelInfoService infoService = new DomainModelInfoService();
			infoService.setConfiguration(manager);
			infoService.setCategoriesCsv(categoriesCsv);
			infoService.initialize();

			DynamicHibernateModelGenerator gen = new DynamicHibernateModelGenerator();
			gen.setInfoService(infoService);
			gen.setIdGenerator(idGen);

			gen.generateMappings(outPath);
		} catch (IOException | ModelDefinitionException e) {
			throw ExceptionUtils.propagate(e);
		}
	}

}
