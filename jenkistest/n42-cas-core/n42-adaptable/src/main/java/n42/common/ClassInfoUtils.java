package n42.common;

import java.lang.reflect.Array;
import java.util.List;

public abstract class ClassInfoUtils {
	
	private ClassInfoUtils() {}

	public static String getterMethodDescriptor(Class<?> type, boolean isList) {
		StringBuilder sb = new StringBuilder("()");
		if (isList) {
			sb.append(classCode(List.class));
		} else {
			sb.append(classCode(type));
		}
		return sb.toString();
	}

	public static String setterMethodDescriptor(Class<?> type, boolean isList) {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		if (isList) {
			sb.append(classCode(List.class));
		} else {
			sb.append(classCode(type));
		}
		sb.append(')');
		sb.append(classCode(void.class));
		return sb.toString();
	}

	public static String getterMethodSignature(Class<?> type, boolean isList) {

		if (!isList) {
			return getterMethodDescriptor(type, false);
		}

		StringBuilder sb = new StringBuilder("()");
		String listCode = classCode(List.class);
		sb.append(listCode.substring(0, listCode.length() - 1));  // remove the ';'
		sb.append('<');
		sb.append(classCode(type));
		sb.append('>');
		sb.append(';');
		return sb.toString();
	}

	public static String setterMethodSignature(Class<?> type, boolean isList) {

		if (!isList) {
			return setterMethodDescriptor(type, false);
		}

		StringBuilder sb = new StringBuilder();
		sb.append('(');
		String listCode = classCode(List.class);
		sb.append(listCode.substring(0, listCode.length() - 1));  // remove the ';'
		sb.append('<');
		sb.append(classCode(type));
		sb.append('>');
		sb.append(';');
		sb.append(')');
		sb.append(classCode(void.class));
		return sb.toString();
	}

	public static String classCode(Class<?> c) {
		if (c == void.class) {
			return "V";
		}

		Class<?> arrayClass = Array.newInstance(c, 0).getClass();
		return arrayClass.getName().substring(1).replace('.', '/');
	}

}
