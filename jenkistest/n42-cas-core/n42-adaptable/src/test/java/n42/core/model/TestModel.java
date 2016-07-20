package n42.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import n42.model.domain.DomainBase;

/**
 * Simple model to test nested models. Can be reused in many scenarios.
 */
public class TestModel extends DomainBase {
	public String value;
	public List<String> values;

	public TestModel subElement;
	public List<TestModel> subElements = new ArrayList<>(0);

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public TestModel getSubElement() {
		return subElement;
	}

	public void setSubElement(TestModel subElement) {
		this.subElement = subElement;
	}

	public List<TestModel> getSubElements() {
		return subElements;
	}

	public void setSubElements(List<TestModel> subElements) {
		this.subElements = subElements;
	}

	@Override
	public Date getN42CreatedDate() {
		return null;
	}

	@Override
	public void setN42CreatedDate(Date date) {
		// noop
	}

	@Override
	public Date getN42LastUpdatedDate() {
		return null;
	}

	@Override
	public void setN42LastUpdatedDate(Date date) {
		// noop
	}

	@Override
	public String getN42Hash() {
		return null;
	}

	@Override
	public void setN42Hash(String hash) {
		// noop
	}

	@Override
	public Integer getN42Id() {
		return null;
	}

	@Override
	public void setN42Id(Integer n42Id) {
		// noop
	}

	@Override
	public Integer getN42Version() {
		return null;
	}

	@Override
	public void setN42Version(Integer n42Version) {
		// noop
	}
}