package org.vaadin.artur.bakery.web;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vaadin.artur.bakery.domain.enumeration.OrderExtras;

@RestController
@RequestMapping("/api/orderextras")
public class OrderExtrasResource {

	@GetMapping("")
	public String[] getAllRoles() {
		List<OrderExtras> list = Arrays.asList(OrderExtras.values());

		List<String> names = list.stream().map(e -> {
			String lowerCase = e.name().toLowerCase(Locale.ENGLISH);
			return e.name().charAt(0) + lowerCase.substring(1);
		}).collect(Collectors.toList());

		return names.toArray(new String[names.size()]);
	}
}
