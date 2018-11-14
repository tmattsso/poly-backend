package org.vaadin.artur.bakery.web;

import java.beans.IntrospectionException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.vaadin.artur.bakery.web.util.Metadata;

public class ResourceWithMetadata {

	private Class<?> cls;

	public ResourceWithMetadata(Class<?> cls) {
		this.cls = cls;
	}

	@GetMapping("/properties")
	public Map<String, Object> getMetadata() throws IntrospectionException {
		return Metadata.getWithConstraints(cls);
	}

}
