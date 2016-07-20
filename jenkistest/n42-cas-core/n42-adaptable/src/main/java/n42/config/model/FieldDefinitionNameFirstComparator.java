package n42.config.model;

import java.util.Comparator;

import n42.services.serialize.IFieldDefinition;

/**
 * Compares two FieldDefinitions by the field name for sorting purposes.
 * Uses field Type when name is identical, then falls back to isList property.
 */
public class FieldDefinitionNameFirstComparator implements Comparator<IFieldDefinition> {

	private static final FieldDefinitionNameFirstComparator INSTANCE = new FieldDefinitionNameFirstComparator();

	public static FieldDefinitionNameFirstComparator getInstance() {
		return INSTANCE;
	}

	@Override
	public int compare(IFieldDefinition f1, IFieldDefinition f2) {
		int nameComp = f1.getName().compareTo(f2.getName());
		if (nameComp != 0) {
			return nameComp;
		}

		int typeComp = f1.getType().hashCode() - f2.getType().hashCode();
		if (typeComp != 0) {
			return typeComp;
		}

		if (!f1.isList() && f2.isList()) {
			return -1;
		} else if (f1.isList() && !f2.isList()) {
			return 1;
		}

		return 0;
	}
}
