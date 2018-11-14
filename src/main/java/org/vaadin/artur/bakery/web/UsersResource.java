package org.vaadin.artur.bakery.web;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vaadin.artur.bakery.domain.UserInfo;
import org.vaadin.artur.bakery.service.UserInfoService;
import org.vaadin.artur.bakery.web.dto.UserDTO;

@RestController
@RequestMapping("/api/users")
public class UsersResource extends ResourceWithMetadata {

	private final UserInfoService userInfoService;
	private EntityManager entityManager;

	public UsersResource(UserInfoService userInfoService, EntityManager manager) {
		super(UserDTO.class);
		this.userInfoService = userInfoService;
		this.entityManager = manager;
	}

	@PostMapping("")
	public UserDTO createUser(@Valid @RequestBody UserDTO user) throws URISyntaxException {
		if (user.getId() != null) {
			throw new IllegalArgumentException("A new user cannot already have an ID");
		}
		return toDTO(userInfoService.save(fromDTO(user, new UserInfo()), user.getNewPassword()));
	}

	@PutMapping("")
	public UserDTO updateUser(@RequestBody UserDTO user) throws URISyntaxException {
		if (user.getId() == null) {
			throw new IllegalArgumentException("Invalid id");
		}
		UserInfo currentUser = userInfoService.findOne(user.getId()).get();
		entityManager.detach(currentUser); // Must detach for correct @version checking

		if (currentUser.isLocked()) {
			throw new IllegalArgumentException("User is locked and cannot be modified");
		}
		UserInfo updatedUser = fromDTO(user, currentUser);
		UserDTO newDto = toDTO(userInfoService.save(updatedUser, user.getNewPassword()));
		return newDto;
	}

	@GetMapping("")
	public Stream<UserDTO> getAllUserInfos() {
		return userInfoService.findAll().stream().map(userInfo -> toDTO(userInfo));
	}

	@GetMapping("/{id}")
	public UserDTO getUser(@PathVariable Long id) {
		Optional<UserInfo> userInfo = userInfoService.findOne(id);
		return toDTO(userInfo.orElse(null));
	}

	@DeleteMapping("/{id}")
	public void deleteUserInfo(@PathVariable Long id) {
		UserInfo currentUser = userInfoService.findOne(id).get();
		entityManager.detach(currentUser); // Must detach for correct @version checking

		if (currentUser.isLocked()) {
			throw new IllegalArgumentException("User is locked and cannot be modified");
		}

		userInfoService.delete(id);
	}

	private static UserDTO toDTO(UserInfo userInfo) {
		if (userInfo == null)
			return null;
		UserDTO dto = new UserDTO();
		dto.setId(userInfo.getId());
		dto.setVersion(userInfo.getVersion());
		dto.setFirstName(userInfo.getFirstName());
		dto.setLastName(userInfo.getLastName());
		dto.setEmail(userInfo.getEmail());
		dto.setRole(userInfo.getRole());
		dto.setLocked(userInfo.isLocked());

		return dto;
	}

	private static UserInfo fromDTO(UserDTO dto, UserInfo userInfo) {
		userInfo.setVersion(dto.getVersion());
		userInfo.setFirstName(dto.getFirstName());
		userInfo.setLastName(dto.getLastName());
		userInfo.setEmail(dto.getEmail());
		userInfo.setRole(dto.getRole());
		userInfo.setLocked(dto.isLocked());

		return userInfo;
	}
}
