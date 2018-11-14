package org.vaadin.artur.bakery.security.authentication;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;

public class AuthorizationFilter implements Filter {

	public static class JWTAuthentication extends AbstractAuthenticationToken {
		private final Claims claims;

		private JWTAuthentication(Collection<? extends GrantedAuthority> authorities, Claims claims) {
			super(authorities);
			setAuthenticated(true);
			this.claims = claims;
		}

		@Override
		public Object getPrincipal() {
			return claims.getSubject();
		}

		@Override
		public Object getCredentials() {
			return null;
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
			throw new ServletException("Expected an HTTP request and response");
		}

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		validateAuthorization(request);
		chain.doFilter(servletRequest, servletResponse);
	}

	/**
	 * Sets the spring authentication object based on the Authorization header
	 * 
	 * @param request
	 */
	private void validateAuthorization(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		if (auth != null && auth.startsWith("Bearer ")) {
			String jwtToken = auth.substring("Bearer ".length());
			try {
				Claims claims = JWTHandler.getClaims(jwtToken);
				SecurityContextHolder.getContext().setAuthentication(claimsToAuthentication(claims));
			} catch (IllegalArgumentException e) {
				// e.printStackTrace();
			}
		}
	}

	private Authentication claimsToAuthentication(Claims claims) {
		Collection<? extends GrantedAuthority> authorities = JWTHandler.getRoles(claims)
				.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		return new JWTAuthentication(authorities, claims);
	}

	@Override
	public void destroy() {
	}

}
