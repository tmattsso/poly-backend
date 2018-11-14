package org.vaadin.artur.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.artur.bakery.domain.UserInfo;
import org.vaadin.artur.bakery.repository.UserInfoRepository;
import org.vaadin.artur.bakery.service.UserInfoService;
import org.vaadin.artur.bakery.web.UsersResource;

@SpringBootApplication
@ComponentScan(basePackageClasses = { UsersResource.class, UserInfoService.class, DataGenerator.class })
@EnableJpaRepositories(basePackageClasses = { UserInfoRepository.class })
@EntityScan(basePackageClasses = { UserInfo.class })
public class App extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(getClass());
	}

	/**
	 * Main method, used to run the application.
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(App.class);
		app.run(args);
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
