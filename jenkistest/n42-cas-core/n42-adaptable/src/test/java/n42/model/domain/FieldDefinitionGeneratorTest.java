package n42.model.domain;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.sql.rowset.serial.SerialBlob;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import com.google.gson.JsonObject;

import n42.common.Charsets;
import n42.config.model.FieldType;
import n42.services.serialize.IFieldDefinition;

import org.junit.Test;

public final class FieldDefinitionGeneratorTest {

	@Test
	public void generateFromPrimitivesList() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", Arrays.asList(1, 2, 3));
		assertEquals("objectName", fd.getName());
		assertEquals("integer", fd.getType());
		assertTrue(fd.isList());
	}

	@Test
	public void generateFromString() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", "the value");
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.STRING.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromPrimitiveBoolean() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", true);
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.BOOLEAN.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromBoolean() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", new Boolean(true));
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.BOOLEAN.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromPrimitiveInteger() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", 14);
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.INTEGER.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromInteger() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", new Integer(14));
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.INTEGER.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromPrimitiveLong() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", 14l);
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.LONG.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromLong() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", new Long(14l));
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.LONG.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromPrimitiveFloat() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", 14.5f);
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.FLOAT.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromFloat() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", new Float(14.5f));
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.FLOAT.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromDate() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", new Date());
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.TIMESTAMP.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromBigDecimal() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", new BigDecimal(14.5f));
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.BIG_DECIMAL.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromBigInteger() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", new BigInteger("14"));
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.BIG_INTEGER.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromBlob() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("objectName", new SerialBlob("14".getBytes(Charsets.UTF8)));
		assertEquals("objectName", fd.getName());
		assertEquals(FieldType.BLOB.getTypeName(), fd.getType());
	}

	@Test
	public void generateFromPersistenceBean() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("n42CreatedDate", PersistenceBean.class, false);
		assertEquals("n42CreatedDate", fd.getName());
		assertEquals(PersistenceBean.class.getName(), fd.getType());
		assertFalse(fd.isList());
	}

	@Test
	public void generateFromPersistenceBeanList() throws Exception {
		IFieldDefinition fd = FieldDefinitionGenerator.generateField("n42CreatedDate", PersistenceBean.class, true);
		assertEquals("n42CreatedDate", fd.getName());
		assertEquals(PersistenceBean.class.getName(), fd.getType());
		assertTrue(fd.isList());
	}
}

class SimpleObject {
	String property;
}

interface IRealModel {
	String getValue();
	void setValue(String value);
}

abstract class PersistenceBean extends DomainBase {
	//we only use class name
}