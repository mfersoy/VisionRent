package com.visionrent.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthTokenFilter extends OncePerRequestFilter {

	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private static final Logger logger=LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String jwtToken=parseJwt(request);
		
		try {
			if(jwtToken!=null && jwtUtils.validateJwtToken(jwtToken)) {
				String email=jwtUtils.getEmailFromToken(jwtToken);
				UserDetails userDetails= userDetailsService.loadUserByUsername(email);
				UsernamePasswordAuthenticationToken authenticationToken=new 
						UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (Exception e) {
			logger.error("User Not Found {}:",e.getMessage());
		}
		
		filterChain.doFilter(request, response);
		
	}

	/*
	Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2IiwiaWF0IjoxNjY2MzU5NDM1LCJleHAiOjE2NjY0NDU4MzV9.dBP30682v0jrET9IoFw-
	kzPXA78AHJdEgdPjskLefMJoQ9u3Z0J9rRrVlrsBClvZT3B0hwTWNSUkKR5ER7M5ig
	*/
	private String parseJwt(HttpServletRequest request) {
		String header= request.getHeader("Authorization");
		if(StringUtils.hasText(header)&&header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
	
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		AntPathMatcher antPathMatcher=new AntPathMatcher();
		return antPathMatcher.match("/register", request.getServletPath())||antPathMatcher.match("/login", request.getServletPath());
	}
	
}
