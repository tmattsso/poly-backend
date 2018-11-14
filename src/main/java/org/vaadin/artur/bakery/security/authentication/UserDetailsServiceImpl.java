package org.vaadin.artur.bakery.security.authentication;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.vaadin.artur.bakery.domain.UserInfo;
import org.vaadin.artur.bakery.service.UserInfoService;

/**
 * Provides a user and its roles (authorities) for a given user in a format
 * understood by Spring Security.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserInfoService userInfoService;

	public UserDetailsServiceImpl(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userInfo = userInfoService.findOneByEmail(username);
		return userInfo.map(info -> createSpringUser(info))
				.orElseThrow(() -> new UsernameNotFoundException("No user with username '" + username + "' found"));
	}

	private UserDetails createSpringUser(UserInfo userInfo) {
		List<SimpleGrantedAuthority> authorities = Collections
				.singletonList(new SimpleGrantedAuthority(userInfo.getRole()));
		return new User(userInfo.getEmail(), userInfo.getPasswordHash(), authorities);
	}

}
