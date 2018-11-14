package org.vaadin.artur.bakery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewRedirectHack {

	@GetMapping({ "/users", "/users/*", "/products", "/products/*", "/dashboard", "/orders/*/*" })
	public String home() {
		return "/";
	}

}
