package n42.model.domain;

import java.sql.Blob;
import java.sql.SQLException;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ReflectionPropertyAccessTest {

	private ReflectionPropertyAccess fixture;

	private SampleModel object;
	private InnerModel complexProperty;
	private InnerMostModel innerMostProperty;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		object = new SampleModel();
		fixture = new ReflectionPropertyAccess(object, SampleModel.class);
	}

	@Test
	public void testGetWithNull() {
		Object result = fixture.get(null);
		Assert.assertEquals(null, result);
	}

	@Test
	public void testGetSimpleAttributes() {
		object.setSimpleProperty("Celestica");

		Object result = fixture.get("simpleProperty");
		Assert.assertEquals("Celestica", result);
	}

	@Test
	public void testGetInnerAttributes() {
		complexProperty = new InnerModel();
		complexProperty.setSimpleProperty("844 Don Mills Road");
		object.setComplexProperty(complexProperty);

		Object result = fixture.get("complexProperty.simpleProperty");
		Assert.assertEquals("844 Don Mills Road", result);
	}

	@Test
	public void testGetInnerMostAttributes() {
		complexProperty = new InnerModel();
		complexProperty.setSimpleProperty("844 Don Mills Road");

		innerMostProperty = new InnerMostModel();
		innerMostProperty.setSimpleProperty("Toronto");
		complexProperty.setInnerMostProperty(innerMostProperty);
		object.setComplexProperty(complexProperty);

		Object result = fixture.get("complexProperty.innerMostProperty.simpleProperty");
		Assert.assertEquals("Toronto", result);
	}
	

	// Models used to test the retrieval of values
	// of fields whose getter methods are retrieved from the
	// model hierarchy

	public class SampleModel {
		private String simpleProperty;
		private InnerModel complexProperty;
		private Blob blob;

		public String getSimpleProperty() {
			return simpleProperty;
		}

		public void setSimpleProperty(String simpleProperty) {
			this.simpleProperty = simpleProperty;
		}

		public InnerModel getComplexProperty() {
			return complexProperty;
		}

		public void setComplexProperty(InnerModel complexProperty) {
			this.complexProperty = complexProperty;
		}
		
		public void setImage(Blob blob) {
			this.blob = blob;
		}
		
		public Blob getImage() {
			return blob;
		}
	}

	public class InnerModel {
		private String simpleProperty;

		private InnerMostModel innerMostProperty;

		public InnerMostModel getInnerMostProperty() {
			return innerMostProperty;
		}

		public void setInnerMostProperty(InnerMostModel innerMostProperty) {
			this.innerMostProperty = innerMostProperty;
		}

		public String getSimpleProperty() {
			return simpleProperty;
		}

		public void setSimpleProperty(String simpleProperty) {
			this.simpleProperty = simpleProperty;
		}
	}

	public class InnerMostModel {
		private String simpleProperty;

		public String getSimpleProperty() {
			return simpleProperty;
		}

		public void setSimpleProperty(String simpleProperty) {
			this.simpleProperty = simpleProperty;
		}
	}
}