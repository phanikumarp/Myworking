package n42.services.model.info;

import java.util.Set;

import n42.model.domain.DomainBase;
import n42.services.serialize.IFieldDefinition;
import n42.services.serialize.IModelDefinition;

public interface InfoService {

	IModelDefinition getDefinition(String modelQualifiedName);

	IModelDefinition getDefinitionByPartialName(String model);

	Set<String> getModels();

	/**
	 * @return all models derived from the given model
	 */
	Set<IModelDefinition> getDerivedModels(String parentQualifiedName);
	
	/**
	 * @return null or parent model
	 */
	IModelDefinition getParentModel(String childQualifiedName);

	Object getFieldValue(Object object, IFieldDefinition field) throws ModelDefinitionException;

	/**
	 * Validate model instance against it's definition.
	 * 
	 * @return empty set if model is perfectly valid, a set of invalid field
	 *         paths otherwise
	 */
	Set<String> validate(DomainBase model, IModelDefinition definition)  throws ModelDefinitionException;
}
