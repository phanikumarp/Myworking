package n42.config.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;


public enum FieldType {

	BOOLEAN(Boolean.class, boolean.class, false),
	INTEGER(Integer.class, int.class, 0),
	LONG(Long.class, long.class, 0l),
	FLOAT(Float.class, float.class, 0f),
	BIG_DECIMAL(BigDecimal.class),
	BIG_INTEGER(BigInteger.class),
	TIMESTAMP(Date.class),
	BLOB(byte[].class),
	STRING(String.class),
	TEXT(String.class);

	private Class<?> nullableJavaType;
	private Class<?> notNullableJavaType;

	private Object defaultValue;

	private FieldType(Class<?> javaType) {
		this.nullableJavaType = javaType;
		this.notNullableJavaType = javaType;
	}

	private FieldType(Class<?> nullableJavaType, Class<?> notNullableJavaType, Object defaultValue) {
		this.nullableJavaType = nullableJavaType;
		this.notNullableJavaType = notNullableJavaType;
		this.defaultValue = defaultValue;
	}

	public Class<?> getJavaType(boolean notNullable) {
		return (notNullable? notNullableJavaType : nullableJavaType);
	}

	public Class<?> getNullableJavaType() {
		return nullableJavaType;
	}

	public Class<?> getNotNullableJavaType() {
		return notNullableJavaType;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return the lower-cased name of enum, the one we use in model types.
	 */
	public String getTypeName() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	public static FieldType fromTypeName(String typeName) throws IllegalArgumentException {
		return valueOf(typeName.toUpperCase(Locale.ENGLISH));
	}

	public static FieldType fromJavaType(Class<?> javaType) {
		for (FieldType ft: values()) {
			//this type enum does not handle model classes (ie. Company or Person) and will return null is such cases
			if (ft.getNotNullableJavaType().equals(javaType) || ft.getNullableJavaType().equals(javaType)) {
				return ft;
			}
		}

		return null;
	}

	/**
	 * Essentially anything that is not a Model would be considered a "basic type" (ex string, text, integer, blob, etc).
	 * One important assumption that was made is that IFieldDefinition will never return type unsupported by this enum
	 * (except for model types, including dynamic models)
	 *
	 * @param typeName
	 * @return
	 * @throws java.lang.IllegalArgumentException if passed {@code typeName == null}.
	 */
	public static boolean isBasicType(String typeName) {
		if (typeName==null) {
			throw new IllegalArgumentException("TypeName can not be null.");
		}

		String enumName = typeName.toUpperCase(Locale.ENGLISH);
		for (FieldType fieldType: FieldType.values()) {
			if (fieldType.name().equals(enumName)) {
				return true;
			}
		}

		return false;
	}

}
