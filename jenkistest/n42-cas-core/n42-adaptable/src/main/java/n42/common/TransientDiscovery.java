package n42.common;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public final class TransientDiscovery {
	private static enum Cached { TRANSIENT, NONTRANSIENT, UNKNOWN }
	private static final Map<String, Map<String, Cached>> CACHE = new HashMap<>();
	
	public static boolean isTransient(Class<?> type, String field) {
		Assert.notNull(type, "type cannot be null");
		Assert.notNull(field, "field cannot be null");
		
		Cached result = consultCaches(type, field);
		if (result == Cached.UNKNOWN) {
			// Try getter
			String getterName = ReflectionUtils.getterName(field, boolean.class.equals(type));
			if (getterName != null) {
				try {
					Method m = type.getMethod(getterName);
					if (m.getAnnotation(Transient.class) != null) {
						result = Cached.TRANSIENT;
					}
				} catch (NoSuchMethodException e) {
					// ignore
				}
			}
			if (result == Cached.UNKNOWN) {
				// Check field
				try {
					Field f = type.getDeclaredField(field);
					if (( f.getModifiers() & Modifier.TRANSIENT ) != 0) {
						result = Cached.TRANSIENT;
					}
				} catch (NoSuchFieldException e) {
					// ignore
				}
			}
			if (result == Cached.UNKNOWN) {
				// assume non-transient
				result = Cached.NONTRANSIENT;
			}
			
			updateCache(type, field, result);
		}
		
		return result == Cached.TRANSIENT;
	}
	
	
	
	private static Cached consultCaches(Class<?> type, String field) {
		String className = type.getName();
		Cached result = null;
		synchronized(CACHE) {
			Map<String, Cached> fields = CACHE.get(className);
			if (fields != null) {
				result = fields.get(field);
			}
		}
		if (result == null) {
			result = Cached.UNKNOWN;
		}
		return result;
	}



	private static void updateCache(Class<?> type, String field, Cached result) {
		if (result != Cached.UNKNOWN) {
			String className = type.getName();
			synchronized(CACHE) {
				Map<String, Cached> fields = CACHE.get(className);
				if (fields == null) {
					fields = new HashMap<>();
					CACHE.put(className, fields);
				}
				fields.put(field, result);
			}
		}
	}



	private TransientDiscovery() {
		// utility class
	}
}
