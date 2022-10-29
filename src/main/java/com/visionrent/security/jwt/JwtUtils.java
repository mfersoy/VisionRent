package com.visionrent.security.jwt;

import com.visionrent.exception.message.ErrorMessage;
import com.visionrent.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
	
	private static final Logger logger=LoggerFactory.getLogger(JwtUtils.class);

	@Value("${visionrent.app.jwtExpirationMs}")
	private long jwtExpirationMs;
	
	@Value("${visionrent.app.jwtScret}")
	private String jwtSecret;
	
	
	public String generateJwtToken(UserDetailsImpl userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date()).
		setExpiration(new Date(new Date().getTime()+jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
	
	public String getEmailFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateJwtToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException e) {
	
			logger.error(String.format(ErrorMessage.JWTTOKEN_ERROR_MESSAGE, e.getMessage()));
		}
		
		return false;
	}
	
}
