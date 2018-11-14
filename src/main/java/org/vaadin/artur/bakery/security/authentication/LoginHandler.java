package org.vaadin.artur.bakery.security.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class LoginHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		ObjectNode obj = JsonNodeFactory.instance.objectNode();
		obj.put("error", exception.getMessage());
		new ObjectMapper().writeValue(response.getWriter(), obj);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		ObjectNode obj = JsonNodeFactory.instance.objectNode();
		obj.put("accessToken", JWTHandler.issueAccessToken(authentication.getName(),
				authentication.getAuthorities().stream().map(auth -> ((SimpleGrantedAuthority) auth).getAuthority())));
		new ObjectMapper().writeValue(response.getWriter(), obj);
	}

}
