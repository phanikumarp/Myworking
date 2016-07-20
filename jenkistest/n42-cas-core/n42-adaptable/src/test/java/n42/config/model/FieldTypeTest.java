package n42.config.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

public class FieldTypeTest {

	@Test
	public void retrieveEnumByName() throws Exception {
		FieldType type = FieldType.fromTypeName("integer");
		assertNotNull(type);
		assertEquals(FieldType.INTEGER, type);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidArgumentThrowsException() throws Exception {
		FieldType.fromTypeName("unknown_field");
		fail("Should have thrown an exception by now");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullArgumentThrowsException() throws Exception {
		FieldType.isBasicType(null);
		fail("Should have thrown an exception by now");
	}

	@Test
	public void typeNameIsLowerCased() throws Exception {
		assertEquals("big_decimal", FieldType.BIG_DECIMAL.getTypeName());
	}

	@Test
	public void checkIfTypeNameIsDeclared() throws Exception {
		assertTrue(FieldType.isBasicType("integer"));
		assertTrue(FieldType.isBasicType("INTEGER"));
		assertTrue(FieldType.isBasicType("blob"));

		assertFalse(FieldType.isBasicType("Company"));
	}

	@Test
	public void twoJavaTypesDefinedFor() throws Exception {
		assertEquals(Integer.class, FieldType.INTEGER.getNullableJavaType());
		assertEquals(int.class, FieldType.INTEGER.getNotNullableJavaType());

		assertEquals(Long.class, FieldType.LONG.getNullableJavaType());
		assertEquals(long.class, FieldType.LONG.getNotNullableJavaType());

		assertEquals(Float.class, FieldType.FLOAT.getNullableJavaType());
		assertEquals(float.class, FieldType.FLOAT.getNotNullableJavaType());

		assertEquals(Boolean.class, FieldType.BOOLEAN.getNullableJavaType());
		assertEquals(boolean.class, FieldType.BOOLEAN.getNotNullableJavaType());
	}

	@Test
	public void singleJavaTypeDefinedFor() throws Exception {
		assertEquals(Date.class, FieldType.TIMESTAMP.getNullableJavaType());
		assertEquals(Date.class, FieldType.TIMESTAMP.getNotNullableJavaType());

		assertEquals(byte[].class, FieldType.BLOB.getNullableJavaType());
		assertEquals(byte[].class, FieldType.BLOB.getNotNullableJavaType());

		assertEquals(String.class, FieldType.STRING.getNullableJavaType());
		assertEquals(String.class, FieldType.STRING.getNotNullableJavaType());

		assertEquals(String.class, FieldType.TEXT.getNullableJavaType());
		assertEquals(String.class, FieldType.TEXT.getNotNullableJavaType());

	}
}