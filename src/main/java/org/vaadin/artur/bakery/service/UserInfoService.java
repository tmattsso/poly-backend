package org.vaadin.artur.bakery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.artur.bakery.domain.UserInfo;
import org.vaadin.artur.bakery.repository.UserInfoRepository;

@Service
@Transactional
public class UserInfoService {

	private final UserInfoRepository userInfoRepository;

	private PasswordEncoder encoder;

	public UserInfoService(UserInfoRepository userInfoRepository, PasswordEncoder encoder) {
		this.userInfoRepository = userInfoRepository;
		this.encoder = encoder;
	}

	public UserInfo save(UserInfo userInfo, String password) {
		if (password != null) {
			userInfo.setPasswordHash(encoder.encode(password));
		} else {
			Optional<UserInfo> existingUser = findOne(userInfo.getId());
			userInfo.setPasswordHash(existingUser.get().getPasswordHash());
		}

		return userInfoRepository.save(userInfo);
	}

	@Transactional(readOnly = true)
	public List<UserInfo> findAll() {
		return userInfoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<UserInfo> findOne(Long id) {
		return userInfoRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<UserInfo> findOneByEmail(String email) {
		return userInfoRepository.findOneByEmail(email);
	}

	public void delete(Long id) {
		userInfoRepository.deleteById(id);
	}
}
