package n42.config.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import n42.services.config.XStreamConfigManager;
import n42.services.model.info.DomainModelInfoService;
import n42.services.serialize.IFieldDefinition;
import n42.services.serialize.IModelDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelDefinitionTest {

	ModelDefinition root;
	ModelDefinition parent;
	ModelDefinition child;

	@Before
	@SuppressWarnings("serial")
	public void setUp() throws Exception {
		root = new ModelDefinition();
		root.setFields(new ArrayList<IFieldDefinition>() {
			{
				add(new FieldDefinition() {
					{
						setName("root.name");
					}
				});
			}
		});

		parent = new ModelDefinition();
		parent.setFields(new ArrayList<IFieldDefinition>() {
			{
				add(new FieldDefinition() {
					{
						setName("parent.name");
					}
				});
				add(new FieldDefinition() {
					{
						setName("parent.type");
					}
				});
			}
		});

		parent.setParent(root);

		child = new ModelDefinition();
		child.setFields(new ArrayList<IFieldDefinition>() {
			{
				add(new FieldDefinition() {
					{
						setName("child.name");
					}
				});
				add(new FieldDefinition() {
					{
						setName("child.type");
					}
				});
				add(new FieldDefinition() {
					{
						setName("child.prop");
					}
				});
			}
		});

		child.setParent(parent);
	}

	@After
	public void tearDown() throws Exception {
		root = null;
		parent = null;
		child = null;
	}

	@Test
	public void testGetAllFields() {
		List<IFieldDefinition> rootFields = root.getAllFields();

		Assert.assertEquals(1, rootFields.size());
		Assert.assertEquals("root.name", rootFields.get(0).getName());

		List<IFieldDefinition> parentFields = parent.getAllFields();

		Assert.assertEquals(3, parentFields.size());
		Assert.assertEquals("root.name", parentFields.get(0).getName());
		Assert.assertEquals("parent.name", parentFields.get(1).getName());
		Assert.assertEquals("parent.type", parentFields.get(2).getName());

		List<IFieldDefinition> childFields = child.getAllFields();

		Assert.assertEquals(6, childFields.size());
		Assert.assertEquals("root.name", childFields.get(0).getName());
		Assert.assertEquals("parent.name", childFields.get(1).getName());
		Assert.assertEquals("parent.type", childFields.get(2).getName());
		Assert.assertEquals("child.name", childFields.get(3).getName());
		Assert.assertEquals("child.type", childFields.get(4).getName());
		Assert.assertEquals("child.prop", childFields.get(5).getName());
	}

	@Test
	public void testGetInheritedFields() {
		List<IFieldDefinition> rootFields = root.getInheritedFields();

		Assert.assertEquals(0, rootFields.size());

		List<IFieldDefinition> parentFields = parent.getInheritedFields();

		Assert.assertEquals(1, parentFields.size());
		Assert.assertEquals("root.name", parentFields.get(0).getName());

		List<IFieldDefinition> childFields = child.getInheritedFields();

		Assert.assertEquals(3, childFields.size());
		Assert.assertEquals("root.name", childFields.get(0).getName());
		Assert.assertEquals("parent.name", childFields.get(1).getName());
		Assert.assertEquals("parent.type", childFields.get(2).getName());
	}


	@Test
	public void nullSafeLookupField() throws Exception {
		Assert.assertNull(root.lookupField(null, false));
	}
}
