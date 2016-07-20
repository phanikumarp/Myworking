package n42.tools.generate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import n42.config.model.FieldDefinition;
import n42.config.model.ModelDefinition;
import n42.services.model.info.InfoService;
import n42.services.model.info.ModelDefinitionException;
import n42.services.serialize.IFieldDefinition;
import n42.services.serialize.IModelDefinition;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DynamicHibernateModelGeneratorTest {

	private Mockery context = new Mockery();
	private InfoService infoService = context.mock(InfoService.class);

	@After
	public void tearDown() {
		context.assertIsSatisfied();
	}

	@Test
	public void verifyThatJsonTypeIsMappedToText() throws IOException, ModelDefinitionException {

		FieldDefinition simpleFieldDefinition = new FieldDefinition();
		simpleFieldDefinition.setName("myFieldName");
		simpleFieldDefinition.setType("json");

		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setPackageName("n42.model.test");
		modelDefinition.setName("MyModelName");
		modelDefinition.setFields(Arrays.<IFieldDefinition> asList(simpleFieldDefinition));

		String myOutputDirectory = "my-output-directory";
		StringWriter output = new StringWriter();
		DynamicHibernateModelGenerator fixture = createFixture(myOutputDirectory, output, modelDefinition);
		fixture.setInfoService(infoService);

		fixture.generateMappings(myOutputDirectory);

		String expectedOutput =
				EXPECTED_HEADER +
				"<class name=\"n42.model.test.MyModelName\" table=\"mymodelname\">\n" +
				N42_ATTRIBUTES +
				"  <property name=\"myFieldName\" type=\"text\"/>\n" +
				"</class>\n" +
				EXPECTED_FOOTER;

		assertEquals(expectedOutput, output.toString().replaceAll("\\r", ""));
	}

	@Test
	public void textTypeIsMappedToText() throws IOException, ModelDefinitionException {
		FieldDefinition simpleFieldDefinition = new FieldDefinition();
		simpleFieldDefinition.setName("myFieldName");
		simpleFieldDefinition.setType("text");

		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setPackageName("n42.model.test");
		modelDefinition.setName("MyModelName");
		modelDefinition.setFields(Arrays.<IFieldDefinition> asList(simpleFieldDefinition));

		String myOutputDirectory = "my-output-directory";
		StringWriter output = new StringWriter();
		DynamicHibernateModelGenerator fixture = createFixture(myOutputDirectory, output, modelDefinition);
		fixture.setInfoService(infoService);

		fixture.generateMappings(myOutputDirectory);

		String expectedOutput =
				EXPECTED_HEADER +
				"<class name=\"n42.model.test.MyModelName\" table=\"mymodelname\">\n" +
				N42_ATTRIBUTES +
				"  <property name=\"myFieldName\" type=\"text\"/>\n" +
				"</class>\n" +
				EXPECTED_FOOTER;

		assertEquals(expectedOutput, output.toString().replaceAll("\\r", ""));
	}

	@Test
	public void testGenerateSimpleProperty() throws IOException, ModelDefinitionException {

		FieldDefinition simpleFieldDefinition = new FieldDefinition();
		simpleFieldDefinition.setName("myFieldName");
		simpleFieldDefinition.setType("string");

		FieldDefinition simpleListFieldDefinition = new FieldDefinition();
		simpleListFieldDefinition.setName("myListFieldName");
		simpleListFieldDefinition.setType("string");
		simpleListFieldDefinition.setList(true);

		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setPackageName("n42.model.test");
		modelDefinition.setName("MyModelName");
		modelDefinition.setFields(Arrays.<IFieldDefinition> asList(simpleFieldDefinition, simpleListFieldDefinition));

		String myOutputDirectory = "my-output-directory";
		StringWriter output = new StringWriter();
		DynamicHibernateModelGenerator fixture = createFixture(myOutputDirectory, output, modelDefinition);
		fixture.setInfoService(infoService);

		fixture.generateMappings(myOutputDirectory);

		String expectedOutput =
				EXPECTED_HEADER +
				"<class name=\"n42.model.test.MyModelName\" table=\"mymodelname\">\n" +
				N42_ATTRIBUTES +
				"  <property name=\"myFieldName\" type=\"java.lang.String\"/>\n" +
				"  <list name=\"myListFieldName\" table=\"mymodelname_mylistfieldname\">\n" +
				"    <key column=\"PARENT_ID\"/>\n" +
				"    <list-index column=\"SORT_ORDER\"/>\n" +
				"    <element type=\"java.lang.String\" column=\"VALUE\"/>\n" +
				"  </list>\n" +
				"</class>\n" +
				EXPECTED_FOOTER;

		assertEquals(expectedOutput, output.toString().replaceAll("\\r", ""));
	}

	@Test
	public void testGenerateComplexProperty() throws IOException, ModelDefinitionException {

		final String nestedTypeName = "MyNestedType";

		FieldDefinition simpleFieldDefinition = new FieldDefinition();
		simpleFieldDefinition.setName("myNestedFieldName");
		simpleFieldDefinition.setType("string");

		final ModelDefinition nestedType = new ModelDefinition();
		nestedType.setName(nestedTypeName);
		nestedType.setFields(Arrays.<IFieldDefinition> asList(simpleFieldDefinition));

		FieldDefinition complexFieldDefinition = new FieldDefinition();
		complexFieldDefinition.setName("myFieldName");
		complexFieldDefinition.setType(nestedTypeName);

		FieldDefinition complexListFieldDefinition = new FieldDefinition();
		complexListFieldDefinition.setName("myListFieldName");
		complexListFieldDefinition.setType(nestedTypeName);
		complexListFieldDefinition.setList(true);

		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setName("MyModelName");
		modelDefinition.setFields(Arrays.<IFieldDefinition> asList(complexFieldDefinition, complexListFieldDefinition));

		String myOutputDirectory = "my-output-directory";
		StringWriter output = new StringWriter();
		DynamicHibernateModelGenerator fixture = createFixture(myOutputDirectory, output, modelDefinition);
		fixture.setInfoService(infoService);

		context.checking(new Expectations() {{
			allowing(infoService).getDefinitionByPartialName(nestedTypeName);
			will(returnValue(nestedType));
		}});

		fixture.generateMappings(myOutputDirectory);

		String expectedOutput =
				EXPECTED_HEADER +
				"<class name=\".MyModelName\" table=\"mymodelname\">\n" +
				N42_ATTRIBUTES +
				"  <many-to-one name=\"myFieldName\" class=\"nullMyNestedType\" column=\"myFieldName_MyNestedType\" cascade=\"all\"/>\n" +
				"  <list name=\"myListFieldName\" inverse=\"false\" cascade=\"all\">\n" +
				"    <key column=\"myListFieldName_MyModelName\"/>\n" +
				"    <list-index column=\"SORT_ORDER\"/>\n" +
				"    <one-to-many class=\"nullMyNestedType\"/>\n" +
				"  </list>\n" +
				"</class>\n" +
				EXPECTED_FOOTER;

		assertEquals(expectedOutput, output.toString().replaceAll("\\r", ""));
	}

	@Test
	public void testGenerateSubclass() throws IOException, ModelDefinitionException {

		final String superTypeName = "MySuperType";

		FieldDefinition superFieldDefinition = new FieldDefinition();
		superFieldDefinition.setName("mySuperFieldName");
		superFieldDefinition.setType("string");

		final ModelDefinition superType = new ModelDefinition();
		superType.setName(superTypeName);
		superType.setFields(Arrays.<IFieldDefinition> asList(superFieldDefinition));

		FieldDefinition subFieldDefinition = new FieldDefinition();
		subFieldDefinition.setName("mySubFieldName");
		subFieldDefinition.setType("string");

		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setName("MyModelName");
		modelDefinition.setFields(Arrays.<IFieldDefinition> asList(subFieldDefinition));
		modelDefinition.setInherits(superTypeName);
		modelDefinition.setPackageName("com.n42.model.");

		String myOutputDirectory = "my-output-directory";
		StringWriter output = new StringWriter();
		DynamicHibernateModelGenerator fixture = createFixture(myOutputDirectory, output, modelDefinition);
		fixture.setInfoService(infoService);

		context.checking(new Expectations() {{
			allowing(infoService).getDefinitionByPartialName(superTypeName);
			will(returnValue(superTypeName));
		}});

		fixture.generateMappings(myOutputDirectory);

		String expectedOutput =
				EXPECTED_HEADER +
				"<subclass name=\"com.n42.model.MyModelName\" extends=\"MySuperType\" discriminator-value=\"com.n42.model.MyModelName\">\n" +
				"  <property name=\"mySubFieldName\" type=\"java.lang.String\"/>\n" +
				"</subclass>\n" +
				EXPECTED_FOOTER;

		assertEquals(expectedOutput, output.toString().replaceAll("\\r", ""));
	}

	@Test
	public void testGenerateComplexPropertyWithLinkType() throws IOException, ModelDefinitionException {

		final String nestedTypeName = "MyNestedType";

		FieldDefinition simpleFieldDefinition = new FieldDefinition();
		simpleFieldDefinition.setName("myNestedFieldName");
		simpleFieldDefinition.setType("string");

		final ModelDefinition nestedType = new ModelDefinition();
		nestedType.setName(nestedTypeName);
		nestedType.setFields(Arrays.<IFieldDefinition> asList(simpleFieldDefinition));

		FieldDefinition complexFieldDefinition = new FieldDefinition();
		complexFieldDefinition.setName("myFieldName");
		complexFieldDefinition.setType(nestedTypeName);
		complexFieldDefinition.setLinkType(true);

		FieldDefinition complexListFieldDefinition = new FieldDefinition();
		complexListFieldDefinition.setName("myListFieldName");
		complexListFieldDefinition.setType(nestedTypeName);
		complexListFieldDefinition.setList(true);
		complexListFieldDefinition.setLinkType(true);

		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setName("MyModelName");
		modelDefinition.setFields(Arrays.<IFieldDefinition> asList(complexFieldDefinition, complexListFieldDefinition));

		String myOutputDirectory = "my-output-directory";
		StringWriter output = new StringWriter();
		DynamicHibernateModelGenerator fixture = createFixture(myOutputDirectory, output, modelDefinition);
		fixture.setInfoService(infoService);

		context.checking(new Expectations() {{
			allowing(infoService).getDefinitionByPartialName(nestedTypeName);
			will(returnValue(nestedType));
		}});

		fixture.generateMappings(myOutputDirectory);

		String expectedOutput =
				EXPECTED_HEADER +
				"<class name=\".MyModelName\" table=\"mymodelname\">\n" +
				N42_ATTRIBUTES +
				"  <list name=\"myListFieldName\" table=\"mymodelname_mylistfieldname\" cascade=\"save-update\">\n" +
				"    <key column=\"myListFieldName_MyModelName\"/>\n" +
				"    <list-index column=\"SORT_ORDER\"/>\n" +
				"    <many-to-many column=\"MyNestedType\" class=\"nullMyNestedType\"/>\n" +
				"  </list>\n" +
				"  <join table=\"mymodelname_mynestedtype\" optional=\"true\">\n" +
				"    <key column=\"mymodelname\"/>\n" +
				"    <many-to-one class=\"nullMyNestedType\" column=\"myfieldname\" name=\"myFieldName\" not-null=\"true\" cascade=\"save-update\" />\n" +
				"  </join>\n" +
				"</class>\n" +
				EXPECTED_FOOTER;

		assertEquals(expectedOutput, output.toString().replaceAll("\\r", ""));
	}

	private DynamicHibernateModelGenerator createFixture(final String expectedOutputDirectory, final Writer output, final ModelDefinition... definitions) {
		return new DynamicHibernateModelGenerator() {

			@Override
			protected Writer createFileWriter(String outputDirectory) throws IOException {
				assertEquals(expectedOutputDirectory, outputDirectory);
				return output;
			}

			@Override
			protected Iterable<IModelDefinition> getModelDefinitions() {
				return Arrays.<IModelDefinition> asList(definitions);
			}
		};
	} 

	private static final String EXPECTED_HEADER =
			"<?xml version=\"1.0\"?>\n" +
			"<!DOCTYPE hibernate-mapping PUBLIC\n" +
			"\"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n" +
			"\"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">\n" +
			"<hibernate-mapping default-cascade=\"all-delete-orphan\">\n";

	private static final String N42_ATTRIBUTES =
			"  <meta attribute=\"extends\" inherit=\"false\">n42.model.domain.DomainBase</meta>\n" +
			"  <id name=\"n42Id\" type=\"integer\" column=\"N42_ID\">\n" +
			"    <generator class=\"n42.services.model.persist.LazyIdGenerator\"/>\n" +
			"  </id>\n" +
			"  <discriminator column=\"TYPE\" type=\"string\"/>\n" +
			"  <version name=\"n42Version\" column=\"N42_VERSION\"/>\n" +
			"  <property name=\"n42CreatedDate\" type=\"timestamp\"/>\n" +
			"  <property name=\"n42LastUpdatedDate\" type=\"timestamp\"/>\n" +
			"  <property name=\"n42Hash\" type=\"string\"/>\n";

	private static final String EXPECTED_FOOTER =
			"<filter-def name=\"n42TenantFilter\">\n" +
			"  <filter-param name=\"n42TenantId\" type=\"string\"/>\n" +
			"</filter-def>\n" +
			"</hibernate-mapping>\n";
}
