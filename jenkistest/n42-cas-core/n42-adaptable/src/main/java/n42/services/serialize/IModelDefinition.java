package n42.services.serialize;

import java.util.List;

public interface IModelDefinition {
	public static final String TYPE_FIELD_NAME = "N42_MODEL_TYPE";

	String getName();

	String getQualifiedName();
	
	String getCache();

	/**
	 * get the fields defined in this model (leaf fields)
	 *
	 * @return
	 */
	List<IFieldDefinition> getFields();

	/**
	 * get the fields defined by all superclasses of this model (parent fields)
	 *
	 * @return
	 */
	List<IFieldDefinition> getInheritedFields();

	/**
	 * get the fields defined by this model and all of its parent models
	 *
	 * @return
	 */
	List<IFieldDefinition> getAllFields();

	/**
	 * Returns the field definition for the field which is this object's domain
	 * key, if it exists
	 *
	 * @return
	 */
	IFieldDefinition getDomainKeyField();

	String getPackageName();

	String getInherits();

	/**
	 * @return null if no parent model or parent model definition
	 */
	IModelDefinition getParent();
	
	IFieldDefinition lookupField(String fieldName, boolean includeInherited);
}
