package n42.config.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import n42.config.ConfigBase;
import n42.services.serialize.IFieldDefinition;

import org.apache.commons.lang3.StringUtils;

public class ModelContainer extends ConfigBase {
	private static final long serialVersionUID = 2937807168127953900L;

	private transient Map<String, ModelDefinition> defMap;

	private String filename;

	private List<ModelDefinition> definitions;
	private List<ModelDefinition> contributions;

	private String modelPackage;

	public ModelDefinition getDefinition(String name) {
		return defMap.get(name);
	}

	public void initializeLookup() {
		defMap = new HashMap<>();
		for (ModelDefinition modelDefinition: definitions) {
			defMap.put(modelDefinition.getName(), modelDefinition);
		}
	}

	public void initializePackageNames() {
		for (ModelDefinition modelDefinition: definitions) {
			modelDefinition.setPackageName(modelPackage);
		}
	}

	/**
	 * Get this container's sub-category. The model container's category is
	 * extracted from the source filename using the following convention:
	 *
	 *  <pre>{sub-sub-category}_..._{sub-category}_{category}_model.xml</pre>
	 *
	 * @param parentCategories
	 * @return
	 * 	the underscore separated sub-category string excluding
	 * 	<code>parentCategories</code>
	 */
	public String getSubCategory(String... parentCategories) {

		String parentCategory = Pattern.quote("model.xml");
		for (String category: parentCategories) {
			parentCategory = Pattern.quote(category) + "_?" + parentCategory;
		}

		Pattern regex = Pattern.compile("(.*_?)(" + parentCategory + ")");
		Matcher m = regex.matcher(filename);
		if (m.matches()) {
			return StringUtils.removeEnd(m.group(1), "_");
		}

		return null;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public String getModelPackage() {
		return modelPackage;
	}

	public List<IFieldDefinition> getFieldDefinitions(String qualifiedClassName) {
		ModelDefinition modelDefinition = findModelDefinition(qualifiedClassName);
		if (modelDefinition == null) {
			return null;
		}

		List<IFieldDefinition> result = new ArrayList<>();
		result.addAll(modelDefinition.getFields());

		return result;
	}

	public ModelDefinition findModelDefinition(String qualifiedClassName) {
		for (ModelDefinition modelDefinition: definitions) {
			if ((getModelPackage() + modelDefinition.getName()).equals(qualifiedClassName)) {
				return modelDefinition;
			}
		}
		return null;
	}

	public ModelDefinition getModelDefinition(String qualifiedClassName) {
		return findModelDefinition(qualifiedClassName);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<ModelDefinition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<ModelDefinition> definitions) {
		this.definitions = definitions;
	}

	public List<ModelDefinition> getContributions() {
		return contributions;
	}

	public void setContributions(List<ModelDefinition> contributions) {
		this.contributions = contributions;
	}

}
