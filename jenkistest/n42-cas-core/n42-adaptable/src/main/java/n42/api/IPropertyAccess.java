package n42.api;

import java.util.Set;

import n42.services.serialize.IFieldDefinition;

public interface IPropertyAccess {

	Object get(String propertyName);

	void set(String propertyName, Object value);

	boolean isReadable(String propertyName);

	boolean isWriteable(String propertyName);

	Set<IFieldDefinition> propertyList();

	IFieldDefinition property(String propertyName);
}
