package org.vaadin.artur.bakery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.artur.bakery.domain.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
	Optional<UserInfo> findOneByEmail(String email);

}
