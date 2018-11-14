package org.vaadin.artur.bakery;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.vaadin.artur.bakery.domain.ShoppingOrder;
import org.vaadin.artur.bakery.domain.UserInfo;
import org.vaadin.artur.bakery.domain.enumeration.OrderExtras;
import org.vaadin.artur.bakery.repository.ShoppingOrderRepository;
import org.vaadin.artur.bakery.repository.UserInfoRepository;

@Component
public class DataGenerator {

	private static final String[] FIRST_NAME = new String[] { "Ori", "Amanda", "Octavia", "Laurel", "Lael", "Delilah",
			"Jason", "Skyler", "Arsenio", "Haley", "Lionel", "Sylvia", "Jessica", "Lester", "Ferdinand", "Elaine",
			"Griffin", "Kerry", "Dominique" };
	private static final String[] LAST_NAME = new String[] { "Carter", "Castro", "Rich", "Irwin", "Moore", "Hendricks",
			"Huber", "Patton", "Wilkinson", "Thornton", "Nunez", "Macias", "Gallegos", "Blevins", "Mejia", "Pickett",
			"Whitney", "Farmer", "Henry", "Chen", "Macias", "Rowland", "Pierce", "Cortez", "Noble", "Howard", "Nixon",
			"Mcbride", "Leblanc", "Russell", "Carver", "Benton", "Maldonado", "Lyons" };
	private static final String ROLE_BAKER = "baker";
	private static final String ROLE_BARISTA = "barista";
	private static final String ROLE_ADMIN = "admin";

	private final Random random = new Random(1L);

	private UserInfoRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private ShoppingOrderRepository shoppingOrders;

	@Autowired
	public DataGenerator(UserInfoRepository userRepository, PasswordEncoder passwordEncoder,
			ShoppingOrderRepository shoppingOrders) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.shoppingOrders = shoppingOrders;
	}

	private Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	@PostConstruct
	public void loadData() {
		if (userRepository.count() != 0L) {
			getLogger().info("Using existing database");
			return;
		}

		getLogger().info("Generating demo data");

		getLogger().info("... generating users");
		UserInfo baker = createBaker(userRepository, passwordEncoder);
		UserInfo barista = createBarista(userRepository, passwordEncoder);
		createAdmin(userRepository, passwordEncoder);
		// A set of products without constrains that can be deleted
		createDeletableUsers(userRepository, passwordEncoder);

		createOrders(shoppingOrders);

	}

	private void createOrders(ShoppingOrderRepository shoppingOrders) {
		ShoppingOrder order = new ShoppingOrder();

		order.setCustomerName("Aslak Reindeer");
		order.setCustomerEmail("aslak@reindeers.com");

		Set<OrderExtras> extras = new HashSet<>();
		extras.add(OrderExtras.DRONE);
		order.setExtras(extras);

		order = shoppingOrders.save(order);

		getLogger().info("New order id is " + order.getId());
	}

	private UserInfo createBaker(UserInfoRepository userRepository, PasswordEncoder passwordEncoder) {
		return userRepository.save(
				createUser("baker@vaadin.com", "Heidi", "Carter", passwordEncoder.encode("baker"), ROLE_BAKER, false));
	}

	private UserInfo createBarista(UserInfoRepository userRepository, PasswordEncoder passwordEncoder) {
		return userRepository.save(createUser("barista@vaadin.com", "Malin", "Castro",
				passwordEncoder.encode("barista"), ROLE_BARISTA, true));
	}

	private UserInfo createAdmin(UserInfoRepository userRepository, PasswordEncoder passwordEncoder) {
		return userRepository.save(
				createUser("admin@vaadin.com", "Göran", "Rich", passwordEncoder.encode("admin"), ROLE_ADMIN, true));
	}

	private void createDeletableUsers(UserInfoRepository userRepository, PasswordEncoder passwordEncoder) {
		userRepository.save(
				createUser("peter@vaadin.com", "Peter", "Bush", passwordEncoder.encode("peter"), ROLE_BARISTA, false));
		userRepository
				.save(createUser("mary@vaadin.com", "Mary", "Ocon", passwordEncoder.encode("mary"), ROLE_BAKER, true));
	}

	private UserInfo createUser(String email, String firstName, String lastName, String passwordHash, String role,
			boolean locked) {
		UserInfo user = new UserInfo();
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPasswordHash(passwordHash);
		user.setRole(role);
		user.setLocked(locked);
		return user;
	}
}
