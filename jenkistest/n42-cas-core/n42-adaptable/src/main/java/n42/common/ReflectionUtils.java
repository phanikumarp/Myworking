package n42.common;

import java.lang.reflect.Method;

public abstract class ReflectionUtils {
	
	private ReflectionUtils() {}

	private static final String PREFIX_GET = "get";
	private static final String PREFIX_IS = "is";
	private static final String PREFIX_SET = "set";

	public static String getterName(String propertyName, boolean isBoolean) {
		return methodName(isBoolean ? PREFIX_IS : PREFIX_GET, propertyName);
	}

	public static String setterName(String propertyName) {
		return methodName(PREFIX_SET, propertyName);
	}

	private static String methodName(String prefix, String propertyName) {

		if (propertyName == null || propertyName.trim().isEmpty()) {
			return null;
		}

		return prefix + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
	}

	private static String decapitalize(String propertyName) {
		// two consecutive uppercase chars indicate an acronym, e.g. getURL -> URL
		if (propertyName.length() > 1 && Character.isUpperCase(propertyName.charAt(0)) && Character.isUpperCase(propertyName.charAt(1))) {
			return propertyName;
		} else {
			return Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
		}
	}

	public static String propertyName(String methodName) {

		if (methodName.length() > PREFIX_IS.length() && methodName.startsWith(PREFIX_IS)) {
			return decapitalize(methodName.substring(PREFIX_IS.length()));
		}

		if (methodName.length() > PREFIX_GET.length() && (methodName.startsWith(PREFIX_GET) || methodName.startsWith(PREFIX_SET))) {
			return decapitalize(methodName.substring(PREFIX_GET.length()));
		}

		return methodName;
	}

	public static boolean isGetter(String methodName, Class<?>[] parameterTypes) {
		final boolean getPrefix = methodName.startsWith(PREFIX_GET) && methodName.length() > PREFIX_GET.length();
		final boolean isPrefix = methodName.startsWith(PREFIX_IS) && methodName.length() > PREFIX_IS.length();

		return  (getPrefix || isPrefix) && parameterTypes.length == 0 && !methodName.equals("getClass");
	}

	public static boolean isGetter(Method method) {
		return isGetter(method.getName(), method.getParameterTypes());
	}

	private static final Class<?>[] EMPTY = new Class<?>[0];
	public static boolean isGetter(String methodName) {
		return isGetter(methodName, EMPTY);
	}
	
	public static Class<?> promoteToClass(Class<?> type) {
		if (type.equals(byte.class)) {
			return Byte.class;
		} else if (type.equals(short.class)) {
			return Short.class;
		} else if (type.equals(int.class)) {
			return Integer.class;
		} else if (type.equals(long.class)) {
			return Long.class;
		} else if (type.equals(float.class)) {
			return Float.class;
		} else if (type.equals(double.class)) {
			return Double.class;
		} else if (type.equals(boolean.class)) {
			return Boolean.class;
		} else {
			return type;
		}
	}
}
