package n42.model.domain;

import java.sql.Blob;
import java.util.List;

import n42.api.IModelTypeInfo;
import n42.config.model.FieldDefinition;
import n42.config.model.FieldType;
import n42.services.serialize.IFieldDefinition;

public final class FieldDefinitionGenerator {

	public static IFieldDefinition generateField(String propertyName, Class<?> type, boolean isList) {
		String typeName = null;

		FieldType fieldType = FieldType.fromJavaType(type);
		if (fieldType == null) {
			if (DomainBase.class.isAssignableFrom(type)) {
				typeName = type.getName();
			} else if (DomainBase.class.isAssignableFrom(type)) {
				typeName = type.getSimpleName();
				if (typeName.startsWith("I")) {
					typeName = typeName.substring(1);
				}
			} else if (Blob.class.isAssignableFrom(type)) {
				typeName = FieldType.BLOB.getTypeName();
			} 
		} else {
			typeName = fieldType.getTypeName();
		}

		return generateField(propertyName, typeName, isList);
	}

	public static IFieldDefinition generateField(String propertyName, String type, boolean isList) {
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setName(propertyName);
		fieldDefinition.setList(isList);
		fieldDefinition.setType(type);

		return fieldDefinition;
	} 

	public static IFieldDefinition generateField(String name, Object value) {
		boolean isList = false;
		Class<?> type = Object.class;  // or should this be null?

		Object valueForType = null;
		if (value instanceof List) {
			isList = true;
			if (((List<?>) value).size() > 0) {
				valueForType = ((List<?>) value).get(0);  // TODO: find common superclass of all elements?
			}
		} else {
			valueForType = value;
		}

		if (valueForType != null) {
			type = valueForType.getClass();

			// If value is adaptable, let's try to get type from model info.
			if (DomainBase.class.isInstance(valueForType)) {
				IModelTypeInfo typeInfo = (IModelTypeInfo)((DomainBase) valueForType);
				return generateField(name, typeInfo.getModelName(), isList);
			}
		}

		return generateField(name, type, isList);
	}

	private FieldDefinitionGenerator() {}

}
