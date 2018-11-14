package org.vaadin.artur.bakery.security.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PROCESSING_URL = "/api/login";
	private static final String LOGOUT_SUCCESS_URL = "/";

	private UserDetailsService userDetailsService;
	private PasswordEncoder encoder;
	private LoginHandler loginHandler;

	public SecurityConfiguration(UserDetailsService userDetailsService, PasswordEncoder encoder,
			LoginHandler loginHandler) {
		this.userDetailsService = userDetailsService;
		this.encoder = encoder;
		this.loginHandler = loginHandler;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();

		// Spring CSRF is disabled as the login view is not generated on the server and
		// cannot thus include the CSRF token
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/api/**").authenticated();
		// Allow all requests by logged in users.
		// .anyRequest().hasAnyAuthority(Role.getAllRoles())

		// Configure the login page
		http.addFilterAfter(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.formLogin().loginProcessingUrl(LOGIN_PROCESSING_URL).failureHandler(loginHandler)
				.successHandler(loginHandler);
		// .loginPage("/");

		// Configure logout
		http.logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}