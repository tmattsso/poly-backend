package org.vaadin.artur.bakery.web.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.Validation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.vaadin.artur.bakery.domain.HumanReadable;

public class Metadata {

	public static Set<java.beans.PropertyDescriptor> get(Class<?> cls) {
		Set<java.beans.PropertyDescriptor> properties = new HashSet<>();
		try {
			Stream.of(Introspector.getBeanInfo(cls).getPropertyDescriptors())
					.filter(pd -> pd.getPropertyType() != Class.class).forEach(p -> properties.add(p));
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static Map<String, Object> getWithConstraints(Class<?> entityClass) {
		Set<java.beans.PropertyDescriptor> props = get(entityClass);
		Map<String, Object> meta = new HashMap<>();
		Map<String, Object> properties = new HashMap<>();
		meta.put("properties", properties);
		meta.put("type", entityClass.getName());

		BeanDescriptor constraints = Validation.buildDefaultValidatorFactory().getValidator()
				.getConstraintsForClass(entityClass);

		props.forEach(pd -> {
			String propertyName = pd.getName();
			HashMap<String, Object> propertyData = new HashMap<>();
			properties.put(propertyName, propertyData);

			String type = pd.getPropertyType().getName();
			propertyData.put("type", type);

			Field f = getField(entityClass, propertyName);
			if (f != null) {
				Id id = f.getAnnotation(Id.class);
				if (id != null) {
					propertyData.put("id", true);
				}

				Version version = f.getAnnotation(Version.class);
				if (version != null) {
					propertyData.put("version", true);
				}
				HumanReadable humanReadable = f.getAnnotation(HumanReadable.class);
				if (humanReadable != null) {
					propertyData.put("humanReadable", true);
				}
			}

			List<Map<String, String>> rules = new ArrayList<>();
			PropertyDescriptor constraintsForProperty = constraints.getConstraintsForProperty(propertyName);
			if (constraintsForProperty != null) {
				for (ConstraintDescriptor<?> d : constraintsForProperty.getConstraintDescriptors()) {
					Annotation ann = d.getAnnotation();
					if (ann instanceof Size) {
						Size size = ((Size) ann);
						Map<String, String> rule = new HashMap<>();
						rule.put("type", "size");
						rule.put("min", "" + size.min());
						rule.put("max", "" + size.max());
						rules.add(rule);
					} else if (ann instanceof Min) {
						Min min = (Min) ann;
						Map<String, String> rule = new HashMap<>();
						rule.put("type", "size");
						rule.put("min", "" + min.value());
						rules.add(rule);
					} else if (ann instanceof Max) {
						Max min = (Max) ann;
						Map<String, String> rule = new HashMap<>();
						rule.put("type", "size");
						rule.put("max", "" + min.value());
						rules.add(rule);
					}
				}
			}

			propertyData.put("constraints", rules);
		});

		return meta;
	}

	private static Field getField(Class<?> cls, String propertyName) {
		if (cls == null)
			return null;
		try {
			return cls.getDeclaredField(propertyName);
		} catch (NoSuchFieldException e) {
			return getField(cls.getSuperclass(), propertyName);
		}
	}

}
