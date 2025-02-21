package az.developia.shop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import az.developia.shop.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

	private final String secretKey;
	private final Key key;
	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
	@Autowired
	private UserRepository userRepository;

	public JwtUtil(@Value("${jwt.secretKey}") String secretKey) {
		if (secretKey == null || secretKey.length() < 32) {
			throw new IllegalArgumentException("Secret key must be at least 32 characters long");
		}
		this.secretKey = secretKey;
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generateToken(String username) {
		List<String> roles = Collections.singletonList("ROLE_USER");

		return Jwts.builder().setSubject(username).claim("roles", roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME * 10)) // 10 часов
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
		} catch (JwtException e) {
			System.out.println("Invalid JWT token: " + e.getMessage());
			return null;
		}
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			System.out.println("JWT validation failed: " + e.getMessage());
			return false;
		}
	}

	public List<String> extractRoles(String token) {
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		return (List<String>) claims.get("roles");
	}

	public Long getUserIdFromUsername(String username) {
	    System.out.println("Extracted username: " + username);  // Логирование username
	    return userRepository.findByUsername(username)
	            .map(user -> user.getId())
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}


}
