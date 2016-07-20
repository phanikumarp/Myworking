package n42.model.domain.utils;

import java.util.ArrayList;
import java.util.List;

import n42.model.domain.DomainBase;
import n42.storage.serialize.DomainBaseFixture;

public class TestModel extends DomainBaseFixture {

	private String field;
	private List<String> list = new ArrayList<>();
	private DomainBase child;
	private List<DomainBase> children = new ArrayList<>();

	public String getField() {
		return field;
	}

	public void setField(final String field) {
		this.field = field;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(final List<String> list) {
		this.list = list;
	}

	public DomainBase getChild() {
		return child;
	}

	public void setChild(final DomainBase child) {
		this.child = child;
	}

	public List<DomainBase> getChildren() {
		return children;
	}

	public void setChildren(final List<DomainBase> children) {
		this.children = children;
	}

}
