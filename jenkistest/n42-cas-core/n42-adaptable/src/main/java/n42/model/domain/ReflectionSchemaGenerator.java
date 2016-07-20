package n42.model.domain;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import n42.common.ReflectionUtils;
import n42.common.TransientDiscovery;
import n42.config.model.FieldDefinition;
import n42.config.model.FieldType;
import n42.services.serialize.IFieldDefinition;

public final class ReflectionSchemaGenerator {

	public static Set<IFieldDefinition> generateSchema(Class<?> containingClass) {
		return recGenerateSchema(containingClass);
	}

	public static Set<IFieldDefinition> recGenerateSchema(Class<?> containingClass) {
		SortedSet<IFieldDefinition> methods = new TreeSet<>();
		for (Method method: containingClass.getMethods()) {
			if (ReflectionUtils.isGetter(method)) {
				String propertyName = ReflectionUtils.propertyName(method.getName());
				if (TransientDiscovery.isTransient(containingClass, propertyName)) {
					continue;
				}
				boolean isList = false;
				Class<?> type = method.getReturnType();
				try {

					if (List.class.isAssignableFrom(type)) {
						isList = true;
						if (method.getGenericReturnType() instanceof ParameterizedType) {
							type = (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
						} else {
							continue;  // unparameterized types are not supported
						}
					}

					if (DomainBase.class.isAssignableFrom(type) && Proxy.isProxyClass(type)) {
						type = DomainBase.class;
					}

					FieldDefinition fieldDefinition = new FieldDefinition();
					fieldDefinition.setName(propertyName);
					fieldDefinition.setList(isList);

					FieldType fieldType = FieldType.fromJavaType(type);
					if (fieldType == null) {
						String typeName = type.getSimpleName();
						if (type.isInterface() && typeName.startsWith("I")) {
							typeName = typeName.substring(1);
						}
						fieldDefinition.setType(typeName);
					} else {
						fieldDefinition.setType(fieldType.getTypeName());
					}


					methods.add(fieldDefinition);
				} catch (ClassCastException e) {
					// ignore
				}
			}
		}

		return Collections.unmodifiableSet(methods);
	}

	private ReflectionSchemaGenerator() {}
}
