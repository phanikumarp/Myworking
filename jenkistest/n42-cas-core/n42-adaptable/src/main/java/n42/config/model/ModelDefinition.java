package n42.config.model;

import java.util.ArrayList;
import java.util.List;

import n42.config.ConfigBase;
import n42.services.serialize.IFieldDefinition;
import n42.services.serialize.IModelDefinition;

import org.apache.commons.lang3.StringUtils;

public class ModelDefinition extends ConfigBase implements IModelDefinition {

	private static final long serialVersionUID = 1937807168127953900L;

	private transient ModelContainer sourceContainer;

	private String name;
	
	private String cache;

	private String description;

	private String inherits;

	private List<IFieldDefinition> fields;

	private transient String packageName;

	private transient ModelDefinition parent;

	@Override
	public String toString() {

		final StringBuilder sb = new StringBuilder();

		sb.append("model ").append(name);
		if (inherits != null) {
			sb.append(" inherits ").append(inherits);
		}
		if (fields != null) {
			sb.append(" {").append('\n');
			for (IFieldDefinition field: fields) {
				sb.append('\t').append(field.toString()).append('\n');
			}
			sb.append('}');
		}
		return sb.toString();
	}

	public ModelContainer getSourceContainer() {
		return sourceContainer;
	}

	public void setSourceContainer(ModelContainer sourceContainer) {
		this.sourceContainer = sourceContainer;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setParent(final ModelDefinition parent) {
		this.parent = parent;
	}

	@Override
	public List<IFieldDefinition> getFields() {
		return fields;
	}

	@Override
	public List<IFieldDefinition> getInheritedFields() {
		List<IFieldDefinition> inheritedFields = getAllFields();
		if (fields != null) {
			inheritedFields.removeAll(fields);
		}
		return inheritedFields;
	}

	@Override
	public List<IFieldDefinition> getAllFields() {
		List<IFieldDefinition> allFields = new ArrayList<>();
		if (parent != null) {
			allFields.addAll(parent.getAllFields());
		}
		if (fields != null) {
			allFields.addAll(fields);
		}
		return allFields;
	}

	public void setFields(final List<IFieldDefinition> fields) {
		this.fields = fields;
	}

	@Override
	public String getQualifiedName() {
		return StringUtils.join(new String[]{getPackageName(), StringUtils.endsWith(packageName, ".") ? "" : ".", getName()});
	}

	public void setInherits(final String inherits) {
		this.inherits = inherits;
	}

	@Override
	public String getInherits() {
		return inherits;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}

	@Override
	public IFieldDefinition getDomainKeyField() {
		for (IFieldDefinition field: fields) {
			if (field.isDomainKey()) {
				return field;
			}
		}
		return null;
	}

	@Override
	public IModelDefinition getParent() {
		return parent;
	}

	@Override
	public IFieldDefinition lookupField(String fieldName, boolean includeInherited) {
		if (StringUtils.isBlank(fieldName)) {
			//CY-20140203: Some sources do rely this behaviour (see ContentAssociationAnalyzer in legal and legalUs as example)
			return null;
		}

		for (IFieldDefinition f : fields) {
			if (fieldName.equals(f.getName())) {
				return f;
			}
		}
		if (includeInherited && parent != null) {
			return parent.lookupField(fieldName, true);
		}
		return null;
	}
}
