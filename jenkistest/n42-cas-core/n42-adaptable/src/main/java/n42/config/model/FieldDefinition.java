package n42.config.model;

import java.util.ArrayList;
import java.util.List;

import n42.config.ConfigBase;
import n42.services.serialize.IFieldDefinition;
import n42.services.serialize.IModelDefinition;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;

public class FieldDefinition extends ConfigBase implements IFieldDefinition {
	private static final long serialVersionUID = 4937807168127953900L;

	private String name;

	private String type;

	private String description;

	private String inverse;

	private boolean list;

	private String fetch;
	
	private String cache;

	private boolean unique;

	private boolean notNullable;

	private boolean hidden;

	private boolean linkType;

	private boolean sortFieldAlso;

	private float boost;

	private boolean domainKey;

	private boolean tenantKey;

	private transient IModelDefinition model;

	public String toString() {
		@SuppressWarnings("serial")
		List<Pair<Boolean, String>> flags = new ArrayList<Pair<Boolean, String>>() {
			{
				add(Pair.of(list, "list"));
				add(Pair.of(unique, "unique"));
				add(Pair.of(!notNullable, "nullable"));
				add(Pair.of(hidden, "hidden"));
				add(Pair.of(linkType, "link"));
				add(Pair.of(sortFieldAlso, "sort"));
				add(Pair.of(domainKey, "key"));
			}
		};

		final StringBuilder flagString = new StringBuilder();
		String sep = "";
		for (Pair<Boolean, String> flag: flags) {
			if (flag.getLeft()) {
				flagString.append(sep).append(flag.getRight());
				sep = ",";
			}
		}

		String boostString = boost > 0.f ? String.format("%sboost=%.1f", sep, boost) : "";
		return String.format("%s %s : %s%s", type, name, flagString, boostString);
	}

	public boolean isSortFieldAlso() {
		return sortFieldAlso;
	}

	public void setSortFieldAlso(boolean sortFieldAlso) {
		this.sortFieldAlso = sortFieldAlso;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}

	public float getBoost() {
		return boost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getInverse() {
		return inverse;
	}

	public void setInverse(String inverse) {
		this.inverse = inverse;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isNotNullable() {
		return notNullable;
	}

	public void setNotNullable(boolean notNullable) {
		this.notNullable = notNullable;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public String getFetch() {
		return fetch;
	}
	
	public void setFetch(String fetch) {
		this.fetch = fetch;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLinkType(boolean linkType) {
		this.linkType = linkType;
	}

	public boolean isLinkType() {
		return linkType;
	}

	public void setModel(IModelDefinition model) {
		this.model = model;
	}

	public boolean isDomainKey() {
		return domainKey;
	}

	public void setDomainKey(boolean domainKey) {
		this.domainKey = domainKey;
	}

	public boolean isTenantKey() {
		return tenantKey;
	}

	public void setTenantKey(final boolean tenantKey) {
		this.tenantKey = tenantKey;
	}

	@Override
	public IModelDefinition getDeclaringModel() {
		return model;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(type).append(list).append(notNullable).append(tenantKey).append(domainKey).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FieldDefinition other = (FieldDefinition) obj;
		return new EqualsBuilder()
				.append(name, other.name)
				.append(type, other.type)
				.append(list, other.list)
				.append(notNullable, other.notNullable)
				.append(tenantKey, other.tenantKey)
				.append(domainKey, other.domainKey)
				.isEquals();
	}


	@Override
	public int compareTo(IFieldDefinition o) {
		return FieldDefinitionNameFirstComparator.getInstance().compare(this, o);
	}
}
