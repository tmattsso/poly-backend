package org.vaadin.artur.bakery.security.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JWTHandler {

	private static final long HOUR_IN_MS = 1000 * 3600;
	private static String secretKey = "foobar";

	public static String issueAccessToken(String username, Stream<String> roles) {
		Date expiration = new Date(System.currentTimeMillis() + HOUR_IN_MS);

		return Jwts.builder().setSubject(username).setExpiration(expiration)
				.claim("roles", roles.collect(Collectors.joining(",")))
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8)).compact();
	}

	public static Stream<String> getRoles(Claims claims) {
		return Stream.of(((String) claims.get("roles")).split(","));
	}

	public static Claims getClaims(String accessToken) {
		try {
			Jwt token = Jwts.parser().setSigningKey(secretKey.getBytes()).parse(accessToken);
			Object body = token.getBody();
			if (body instanceof Claims) {
				return (Claims) body;
			}
		} catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid JWT token", e);
		}
		throw new IllegalArgumentException("Invalid JWT token");
	}
}
