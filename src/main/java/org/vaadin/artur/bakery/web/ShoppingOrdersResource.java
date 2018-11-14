package org.vaadin.artur.bakery.web;

import java.net.URISyntaxException;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vaadin.artur.bakery.domain.ShoppingOrder;
import org.vaadin.artur.bakery.repository.ShoppingOrderRepository;
import org.vaadin.artur.bakery.web.dto.ShoppingOrderDTO;

@RestController
@RequestMapping("/api/orders")
public class ShoppingOrdersResource extends ResourceWithMetadata {

	private final ShoppingOrderRepository ordersRepo;
	private EntityManager entityManager;

	public ShoppingOrdersResource(ShoppingOrderRepository repo, EntityManager manager) {
		super(ShoppingOrder.class);
		this.ordersRepo = repo;
		this.entityManager = manager;
	}

	@PutMapping("")
	public ShoppingOrderDTO updateUser(@RequestBody ShoppingOrderDTO user) throws URISyntaxException {
		if (user.getId() == null) {
			throw new IllegalArgumentException("Invalid id");
		}
		ShoppingOrder currentUser = ordersRepo.findById(user.getId()).get();
		entityManager.detach(currentUser); // Must detach for correct @version checking

		ShoppingOrder updatedUser = fromDTO(user, currentUser);
		ShoppingOrderDTO newDto = toDTO(ordersRepo.save(updatedUser));
		return newDto;
	}

	@GetMapping("/{id}")
	public ShoppingOrderDTO getUser(@PathVariable Long id) {
		Optional<ShoppingOrder> userInfo = ordersRepo.findById(id);
		return toDTO(userInfo.orElse(null));
	}

	@DeleteMapping("/{id}")
	public void deleteUserInfo(@PathVariable Long id) {
		ShoppingOrder currentUser = ordersRepo.findById(id).get();
		entityManager.detach(currentUser); // Must detach for correct @version checking

		ordersRepo.delete(currentUser);
	}

	private static ShoppingOrderDTO toDTO(ShoppingOrder order) {
		if (order == null)
			return null;
		ShoppingOrderDTO dto = new ShoppingOrderDTO();
		dto.setId(order.getId());
		dto.setVersion(order.getVersion());

		dto.setCustomerName(order.getCustomerName());
		dto.setCustomerEmail(order.getCustomerEmail());
		dto.setExtras(order.getExtras());
		dto.setCompany(order.isCompany());
		dto.setVatID(order.getVatID());

		return dto;
	}

	private static ShoppingOrder fromDTO(ShoppingOrderDTO dto, ShoppingOrder order) {
		order.setVersion(dto.getVersion());
		order.setCustomerName(dto.getCustomerName());
		order.setCustomerEmail(dto.getCustomerEmail());
		order.setExtras(dto.getExtras());
		order.setCompany(dto.isCompany());
		order.setVatID(dto.getVatID());

		return order;
	}

}
