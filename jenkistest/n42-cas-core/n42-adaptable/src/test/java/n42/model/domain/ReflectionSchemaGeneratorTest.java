package n42.model.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import n42.api.IPropertyAccess;
import n42.services.serialize.IFieldDefinition;
import org.junit.Test;

public class ReflectionSchemaGeneratorTest {

	@Test
	public void testSchemaGeneration() {
		Set<IFieldDefinition> properties = ReflectionSchemaGenerator.generateSchema(TestModel.class);
		assertEquals("[boolean boolean : nullable, boolean booleanList : list,nullable, integer integer : nullable, integer integerList : list,nullable," +
			" TestModel nestedModel : nullable, TestModel nestedModelList : list,nullable, string string : nullable, string stringList : list,nullable]",
			properties.toString()
		);
	}

	
	@Test
	public void testSchemaGenerationWithSuperclass() {
		Set<IFieldDefinition> properties = ReflectionSchemaGenerator.generateSchema(SubTestModel.class);
		assertEquals("[boolean boolean : nullable, boolean booleanList : list,nullable, integer integer : nullable, integer integerList : list,nullable," +
			" TestModel nestedModel : nullable, TestModel nestedModelList : list,nullable, string string : nullable, string stringList : list,nullable, " +
			"string subString : nullable]",
			properties.toString()
		);
	}
	

	private static interface TestModel  {

		String getString();

		int getInteger();

		boolean getBoolean();

		List<Integer> getIntegerList();

		List<String> getStringList();

		List<Boolean> getBooleanList();

		TestModel getNestedModel();

		List<TestModel> getNestedModelList();
	}
	
	private static interface SubTestModel extends TestModel {

		String getSubString();
		
	}
}
