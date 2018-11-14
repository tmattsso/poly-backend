package org.vaadin.artur.bakery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.artur.bakery.domain.ShoppingOrder;

public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Long> {

}
