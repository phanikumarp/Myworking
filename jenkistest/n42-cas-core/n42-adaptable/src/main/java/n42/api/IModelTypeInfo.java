package n42.api;

import n42.services.serialize.IModelDefinition;

/**
 * Provides information on model type.
 * 
 * Basically, having <code>IAdaptable model</code> you can always adapt it o
 * {@link IModelTypeInfo}:
 * </p>
 * <code><pre>
 * IAdaptable model = ...;
 * IModelTypeInfo infp = model.adaptTo(IModelTypeInfo.class);
 * String modelType = info.getFullyQualifiedModelName(); 
 * </pre></code>
 * 
 * @author ipsg
 * 
 */
public interface IModelTypeInfo  {
	/**
	 * @return {@link IModelDefinition} of this object
	 */
	IModelDefinition getModelDefinition();

	/**
	 * @return fully qualified model name
	 */
	String getModelName();
}
