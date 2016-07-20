package n42.services.serialize;

public interface IFieldDefinition extends Comparable <IFieldDefinition> {

	String getName();

	String getType();

	String getDescription();

	boolean isLinkType();

	boolean isList();

	String getFetch();
	
	String getCache();

	boolean isSortFieldAlso();

	float getBoost();

	String getInverse();

	boolean isHidden();

	boolean isNotNullable();

	boolean isUnique();

	boolean isDomainKey();

	boolean isTenantKey();

	IModelDefinition getDeclaringModel();
}
