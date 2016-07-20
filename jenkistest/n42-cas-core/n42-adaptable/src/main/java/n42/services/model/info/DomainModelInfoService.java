package n42.services.model.info;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import n42.api.IPropertyAccess;
import n42.api.PropertyAccessException;
import n42.config.model.FieldDefinition;
import n42.config.model.ModelContainer;
import n42.config.model.ModelDefinition;
import n42.model.domain.DomainBase;
import n42.services.config.ConfigException;
import n42.services.config.ConfigManager;
import n42.services.config.XStreamConfigManager;
import n42.services.serialize.IFieldDefinition;
import n42.services.serialize.IModelDefinition;
import n42.services.utils.ExceptionUtils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

public class DomainModelInfoService implements InfoService {
	private static final Logger LOG = LoggerFactory.getLogger(DomainModelInfoService.class);

	public static final Class[] MODEL_CONFIG_CLASSES = new Class[]{
			ModelContainer.class, ModelDefinition.class, FieldDefinition.class
	};

	/**
	 * These field names are either part of {@link DomainBase} or are special names used by the persistence layer.
	 * All names here are lower-cased since DB column names are case insensitive
	 */
	private static final List<String> RESERVED_FIELD_NAMES = Arrays.asList("oiqid", "oiq_id", "oiqcreateddate", "oiqlastupdateddate", "oiqhash",
			"oiqversion", "oiq_version", "type");

	public static boolean isReservedName(String name) {
		return RESERVED_FIELD_NAMES.contains(name.toLowerCase(Locale.ENGLISH));
	}

	private ConfigManager configuration;
	private ClassLoader classLoader;

	private final List<Resource> resources;

	private final Map<String, ModelContainer> containers;
	private final Map<String, ModelDefinition> definitions;
	private final Map<String, Set<IModelDefinition>> derived;
	private final List<ModelDefinition> contributions;

	public DomainModelInfoService() {
		this.containers = new LinkedHashMap<>();
		this.definitions = new LinkedHashMap<>();
		this.contributions = new ArrayList<>();
		this.resources = new ArrayList<>();
		this.derived = new HashMap<>();
	}

	public void setConfiguration(ConfigManager configuration) {
		this.configuration = configuration;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * This setter is used in spring.xml to initialize the service with multiple
	 * model config categories or absolute paths.
	 *
	 * Equivalent to: <code>setCategories(StringUtils.split(csv, ","))</code>.
	 *
	 * @param csv
	 *            comma separated list of model categories or absolute paths.
	 * @see {@link DomainModelInfoService#addCategories(String...)
	 */
	public void setCategoriesCsv(String csv) throws IOException {
		addCategories(StringUtils.split(csv, ","));
	}

	/**
	 * Specify the model categories/paths to load. Each item is considered
	 * as a file path, if the files exists it is added to the list of model
	 * resources to load. Otherwise the item is is interpreted as a model
	 * category. For each category `c', the classpath is searched for resources
	 * matching the pattern:
	 *
	 *  <pre>classpath*:/oiq/&#42;&#42;/model/*<i>c</i>_model.xml</pre>
	 *
	 * each resource that is found is added to the list of model resources to
	 * load.
	 *
	 * @param categories a list of model resource files or categories
	 * @throws IOException
	 */
	public void addCategories(String... categories) throws IOException {
		for (String category: categories) {
			if (new File(category).exists()) {
				this.resources.add(new FileSystemResource(category));
			} else if (StringUtils.isNotBlank(category)) {
				ClassLoader resolverClassLoader = classLoader != null ? classLoader : getClass().getClassLoader();
				PathMatchingResourcePatternResolver categoryResolver = new PathMatchingResourcePatternResolver(resolverClassLoader);

				String categoryPattern = String.format("%s/oiq/**/model/*%s_model.xml", ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX, category);
				Resource[] categoryResources = categoryResolver.getResources(categoryPattern);

				this.resources.addAll(Arrays.asList(categoryResources));

				LOG.info("Discovered {} resources in model category '{}'", categoryResources.length, category);
			}
		}
	}

	public void addResourcePath(String path) {
		File file = new File(path);
		if (file.exists()) {
			this.resources.add(new FileSystemResource(file));
		} else {
			this.resources.add(new ClassPathResource(path, classLoader != null ? classLoader : getClass().getClassLoader()));
		}
	}

	private static void validateFields(ModelContainer modelContainer, ModelDefinition modelDefinition) throws ModelDefinitionException {

		for (IFieldDefinition fieldDefinition: modelDefinition.getFields()) {
			if (StringUtils.isBlank(fieldDefinition.getName())) {
				throw new ModelDefinitionException(String.format(
						"In container: `%s': in model `%s': field definition with no name",
						modelContainer.getFilename(),
						modelDefinition.getQualifiedName()));
			}
			if (isReservedName(fieldDefinition.getName())) {
				throw new ModelDefinitionException(String.format(
						"In container: `%s': in model `%s': illegal field definition: `%s' is a reserved field name",
						modelContainer.getFilename(),
						modelDefinition.getQualifiedName(),
						fieldDefinition.getName()));
			}
			((FieldDefinition) fieldDefinition).setModel(modelDefinition);
		}

	}

	private void appendDefinitions(ModelContainer container) throws ModelDefinitionException {
		for (ModelDefinition modelDefinition: container.getDefinitions()) {
			modelDefinition.setSourceContainer(container);
			modelDefinition.setPackageName(container.getModelPackage());

			validateFields(container, modelDefinition);

			String qualifiedName = modelDefinition.getQualifiedName();
			if (!definitions.containsKey(qualifiedName)) {
				definitions.put(qualifiedName, modelDefinition);
				addToDerived(modelDefinition);
			} else {
				// we can't have multiple models with same name defined
				String modelName = modelDefinition.getPackageName().concat(modelDefinition.getName());
				throw new ModelDefinitionException(String.format(
						"Can't load model '%s' from %s. This model is already loaded from another configuration file.", modelName, container.getFilename()));
			}
		}
	}

	private void addToDerived(ModelDefinition modelDefinition) {
		if (StringUtils.isBlank(modelDefinition.getInherits())) {
			return;
		}
		Set<IModelDefinition> children = derived.get(modelDefinition.getInherits());
		if (children == null) {
			// The default ModelDefinition does not have equals method that works
			// on quelifiedName, that's why we need to specify our own comparator
			children = new TreeSet<>(new ModelDefinitionComparatorByQualifiedName());
			derived.put(modelDefinition.getInherits(), children);
		}
		children.add(modelDefinition);
	}

	private void appendContributions(ModelContainer container) throws ModelDefinitionException {
		for (ModelDefinition modelDefinition: container.getContributions()) {
			modelDefinition.setSourceContainer(container);

			validateFields(container, modelDefinition);

			// Until we have fully removed the intermediate model
			// layers, the contributions need to be able to specify
			// the fully qualified name of the target model
			int packageSplit = modelDefinition.getName().lastIndexOf('.');
			if (packageSplit > 0) {

				String packageName = modelDefinition.getName().substring(0, packageSplit + 1);
				String name = modelDefinition.getName().substring(packageSplit + 1);

				modelDefinition.setPackageName(packageName);
				modelDefinition.setName(name);

			} else {
				modelDefinition.setPackageName(container.getModelPackage());
			}
			contributions.add(modelDefinition);
		}
	}

	public void initialize() throws ModelDefinitionException {
		loadResources();
		injectContributions();
		processInheritence();
	}

	public void initializeSingleton() throws ModelDefinitionException {
		initialize();
		setService(this);
	}

	private void loadResources() throws ModelDefinitionException {
		for (Resource res: resources) {
			try {
				LOG.info("Loading {}", res);

				// because classpaths during build time are somewhat variable
				// (e.g. during a clean there is nothing in output folder, but
				// during an incremental build there is), we skip containers
				// that we've already seen.
				if (containers.containsKey(res.getFilename())) {
					LOG.warn("Ignoring duplicate model container: {}", res);
					continue;
				}

				ModelContainer container = (ModelContainer) configuration.loadConfig(res.getInputStream());
				container.setFilename(res.getFilename());

				containers.put(container.getFilename(), container);

				// Some containers may not specify any model definitions
				// (e.g. a container might only specify model contributions)
				if (container.getDefinitions() == null) {
					container.setDefinitions(new ArrayList<ModelDefinition>(0));
				}
				if (container.getContributions() == null) {
					container.setContributions(new ArrayList<ModelDefinition>(0));
				}

				appendDefinitions(container);
				appendContributions(container);
			} catch (IOException | ConfigException e) {
				String msg = String.format("Error loading %s", res);
				LOG.error(msg, e);
				throw new ModelDefinitionException(msg, e);
			}
		}
	}

	/**
	 * Iterate over the models and inject matching model contribution fields
	 *
	 * @throws ModelDefinitionException
	 */
	private void injectContributions() throws ModelDefinitionException {

		for (ModelDefinition contrib: contributions) {
			ModelDefinition model = definitions.get(contrib.getQualifiedName());
			if (model == null) {
				LOG.warn("Ignoring contribution '{}': there is no such model", contrib.getQualifiedName());
				continue;
			}

			for (IFieldDefinition field: contrib.getFields()) {
				if (model.lookupField(field.getName(), true) != null) {
					throw new ModelDefinitionException(String.format("Contribution %s#%s: field already exists in model: %s (or its parent)",
							contrib.getQualifiedName(), field.getName(), model.getQualifiedName()));
				}

				LOG.info("Augmenting model {} with field {}", model.getQualifiedName(), field);

				model.getFields().add(field);
				((FieldDefinition) field).setModel(model);
			}
		}
	}

	private void processInheritence() {

		for (String modelName: definitions.keySet()) {
			ModelDefinition model = definitions.get(modelName);
			if (model.getInherits() != null) {
				model.setParent(definitions.get(model.getInherits()));
			} else {
				for (Method m: DomainBase.class.getMethods()) {
					if (StringUtils.startsWith(m.getName(), "getOiq")) {
						String fieldName = StringUtils.uncapitalize(m.getName().replaceAll("^get", ""));
						String fieldType = "string";
						Class<?> type = m.getReturnType();
						if (ClassUtils.isAssignable(type, Number.class)) {
							fieldType = "integer";
						} else if (ClassUtils.isAssignable(type, Date.class)) {
							fieldType = "timestamp";
						}

						IFieldDefinition field = buildIField(fieldName, fieldType, model);
						model.getFields().add(field);
					}
				}
			}
		}
	}

	private static IFieldDefinition buildIField(String name, String type, ModelDefinition model) {
		FieldDefinition result = new FieldDefinition();

		result.setName(name);
		result.setType(type);

		if (model != null) {
			result.setModel(model);
		}

		return result;
	}

	@Override
	public IModelDefinition getDefinition(String model) {
		return definitions.get(model);
	}

	@Override
	public Set<String> getModels() {
		return definitions.keySet();
	}

	@Override
	public IModelDefinition getDefinitionByPartialName(String simpleName) {
		// CORE 252 MB we should go looking for largest version of whatever we have.
		for (String modelName: definitions.keySet()) {
			String simpleModelName = modelName.substring(modelName.lastIndexOf(".") + 1);
			if (simpleModelName.equalsIgnoreCase(simpleName)) {
				return definitions.get(modelName);
			}
		}
		return null;
	}

	/**
	 * Same as <code>mergeContainers(null)<code>.
	 *
	 * @see #mergeContainers(String)
	 *
	 * @return
	 */
	public ModelContainer mergeContainers() {
		return mergeContainers(null);
	}

	/**
	 * Merge model containers matching the given domain into one large
	 * container. A model belongs to domain `d' if the resource from which it
	 * was loaded matches the regular expression:<br/>
	 *
	 * <pre>
	 * <i>d</i>.*_model.xml
	 * </pre>
	 *
	 * where <i>d</i> is the value of the domain parameter. If the domain
	 * parameter is null, then all containers are merged.
	 *
	 * @param domain
	 *            the domain to match, or null if all containers should be
	 *            included.
	 * @return
	 */
	public ModelContainer mergeContainers(String domain) {
		ModelContainer container = new ModelContainer();

		container.setDefinitions(new ArrayList<ModelDefinition>());
		container.setContributions(new ArrayList<ModelDefinition>());

		Set<Entry<String, ModelContainer>> entrySet = containers.entrySet();
		for (Entry<String, ModelContainer> entry: entrySet) {

			String containerResource = entry.getKey();
			if (domain != null && !containerResource.matches(String.format("%s.*_model.xml", domain))) {
				continue;
			}

			ModelContainer childContainer = entry.getValue();
			childContainer.initializePackageNames();

			List<ModelDefinition> childDefs = childContainer.getDefinitions();
			List<ModelDefinition> childContribs = childContainer.getContributions();

			if (childDefs != null) {
				container.getDefinitions().addAll(childDefs);
			}
			if (childContribs != null) {
				container.getContributions().addAll(childContribs);
			}
		}
		container.initializeLookup();
		return container;
	}

	@Override
	public Object getFieldValue(Object object, IFieldDefinition field) throws ModelDefinitionException {
		if (object instanceof DomainBase) {
			IPropertyAccess pa = (IPropertyAccess)((DomainBase) object);
			return pa.get(field.getName());
		} else {
			// Otherwise, use reflection
			String methodName = getAccessorFieldMethodName(field.getName());
			try {
				Method method = object.getClass().getMethod(methodName, new Class[0]);
				return method.invoke(object, new Object[0]);
			} catch (IllegalAccessException iae) {
				throw new ModelDefinitionException("Could not access method " + methodName, iae);
			} catch (InvocationTargetException ite) {
				throw new ModelDefinitionException("Could not invoke method " + methodName, ite);
			} catch (NoSuchMethodException nsme) {
				throw new ModelDefinitionException("No method found " + methodName, nsme);
			}
		}
	}

	@Override
	public Set<String> validate(DomainBase model, IModelDefinition definition) throws ModelDefinitionException {
		Assert.notNull(model, "model cannot be null");
		Assert.notNull(definition, "definition cannot be null");

		Set<String> result = new HashSet<>();
		validateImpl(result, "", model, definition);
		return result;
	}

	@Override
	public Set<IModelDefinition> getDerivedModels(String parentQualifiedName) {
		if (!definitions.containsKey(parentQualifiedName)) {
			throw new IllegalArgumentException(parentQualifiedName + " is not a fully qualified model name");
		}
		Set<IModelDefinition> set = derived.get(parentQualifiedName);
		if (set == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(set);
		}
	}

	@Override
	public IModelDefinition getParentModel(String childQualifiedName) {
		IModelDefinition childDef = definitions.get(childQualifiedName);
		if (childDef == null) {
			throw new IllegalArgumentException(childQualifiedName + " model not found");
		}
		return getParentModel(childDef);
	}

	private IModelDefinition getParentModel(IModelDefinition childDef) {
		String parentName = childDef.getInherits();
		return (parentName == null) ? null : definitions.get(parentName);
	}

	private static String getAccessorFieldMethodName(String fieldName) {
		return "get" + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH) + fieldName.substring(1);
	}

	private void validateImpl(Set<String> accumulator, String currentPath, Object model, IModelDefinition definition)
			throws ModelDefinitionException {

		for (IFieldDefinition fdef : definition.getAllFields()) {
			String fieldPath = currentPath.isEmpty() ? fdef.getName() : currentPath + "." + fdef.getName();
			Object fieldValue;
			try { 
				fieldValue = getFieldValue(model, fdef);
			} catch (PropertyAccessException e) {
				throw new ModelDefinitionException("Invalid model "+model, e);
			}
			IModelDefinition fieldModel = getDefinitionByPartialName(fdef.getType());
			if (fieldValue == null) {
				if (fdef.isNotNullable()) {
					accumulator.add(fieldPath);
				}
			} else if (fieldModel != null) {
				if (fdef.isList()) {
					int i = 0;
					for (Object element : (Collection<?>) fieldValue) {
						if (element != null) {
							validateImpl(accumulator, fieldPath+"["+i+"]", element, fieldModel);
						}
						i++;
					}
				} else {
					validateImpl(accumulator, fieldPath, fieldValue, fieldModel);
				}
			}
		}
	}

	private static InfoService instance;

	public static void setService(InfoService service) {
		instance = service;
	}

	public static InfoService getService() {
		return (instance == null) ? Bootstrap.getService() : instance;
	}

	private static class Bootstrap {
		private static final DomainModelInfoService INSTANCE;

		static {
			XStreamConfigManager configuration = new XStreamConfigManager();
			configuration.registerClasses(MODEL_CONFIG_CLASSES);

			INSTANCE = new DomainModelInfoService();
			INSTANCE.setConfiguration(configuration);
			try {
				INSTANCE.addCategories("core", "company", "property", "person", "insure");
				INSTANCE.initialize();
			} catch (IOException | ModelDefinitionException e) {
				LOG.warn(ExceptionUtils.errorLogEntry(e));
			}
		}

		public static InfoService getService() {
			return INSTANCE;
		}
	}

	private static class ModelDefinitionComparatorByQualifiedName implements Comparator<IModelDefinition>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(IModelDefinition o1, IModelDefinition o2) {
			return o1.getQualifiedName().compareTo(o2.getQualifiedName());
		}

	}

}

