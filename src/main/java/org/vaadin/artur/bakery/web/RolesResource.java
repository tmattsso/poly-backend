package org.vaadin.artur.bakery.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RolesResource {

	@GetMapping("")
	public String[] getAllRoles() {
		return new String[] { "baker", "barista", "admin" };
	}
}
