package n42.model.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import n42.api.IPropertyAccess;
import n42.api.PropertyAccessException;
import n42.common.ReflectionUtils;
import n42.services.serialize.IFieldDefinition;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionPropertyAccess implements IPropertyAccess {

	private static final Logger LOG = LoggerFactory.getLogger(ReflectionPropertyAccess.class);

	private final Object object;
	private final Class<?> type;

	public ReflectionPropertyAccess(Object object, Class<?> type) {
		this.object = object;
		this.type = type;
	}

	/**
	 * @throws PropertyAccessException Most of the time it indicates an
	 * unexpected runtime error. In the PropertyHolder decorator example it
	 * should be caught and handled.
	 */
	@Override
	public Object get(String propertyName) {
		if (StringUtils.isBlank(propertyName)) {
			return null;
		}
		try {
			return PropertyUtils.getNestedProperty(object, propertyName);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new PropertyAccessException("get " + propertyName, e);
		}
	}

	/**
	 * @throws PropertyAccessException Most of the time it indicates an
	 * unexpected runtime error. In the PropertyHolder decorator example it
	 * should be caught and handled.
	 */
	@Override
	public void set(String propertyName, Object value) {
		if (StringUtils.isBlank(propertyName)) {
			return;
		}
		try {
			PropertyUtils.setNestedProperty(object, propertyName, value);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new PropertyAccessException("get " + propertyName, e);
		}

	}


	@Override
	public Set<IFieldDefinition> propertyList() {
		return ReflectionSchemaGenerator.generateSchema(type);
	}

	@Override
	public boolean isReadable(String propertyName) {
		return PropertyUtils.isReadable(object, propertyName);
	}

	@Override
	public boolean isWriteable(String propertyName) {
		boolean writeable = PropertyUtils.isWriteable(object, propertyName);
		if (!writeable) {
			LOG.debug("Property {} is not writeable on {}", propertyName, object.getClass());
		}
		return writeable;
	}

	@Override
	public IFieldDefinition property(String propertyName) {
		IFieldDefinition descriptor = null;
		try {
			Class<?> cls = PropertyUtils.getPropertyType(object, propertyName);
			boolean isList = false;
			if (List.class.isAssignableFrom(cls)) {
				isList = true;
				Method getter = type.getMethod(ReflectionUtils.getterName(propertyName, false));
				cls = (Class<?>) ((ParameterizedType) getter.getGenericReturnType()).getActualTypeArguments()[0];
			}
			descriptor = FieldDefinitionGenerator.generateField(propertyName, cls, isList);
		} catch (Exception e) {
			try {
				Object o = PropertyUtils.getNestedProperty(object, propertyName);
				descriptor = FieldDefinitionGenerator.generateField(propertyName, o);
			} catch (Exception e2) {
				// ignore
			}
		}
		return descriptor;
	}
}