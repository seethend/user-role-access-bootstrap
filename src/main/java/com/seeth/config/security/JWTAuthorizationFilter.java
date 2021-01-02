package com.seeth.config.security;

import static com.seeth.config.security.SecurityConstants.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.seeth.user.personal.services.UserService;

import io.jsonwebtoken.Jwts;

/**
 * 
 *
 * Once authentication is completed successfully, every request from same user will have JSON web token in request header
 * 
 * {@link #doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)} method will parse the JSON web token
 * and checks validation
 *
 * @author Seethend Reddy D
 * <br>
 * 03-Dec-2018 6:43:49 PM
 *
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserService customUserDetailService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserService customUserDetailService) {
        super(authenticationManager);
        this.customUserDetailService = customUserDetailService;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		/*
		System.out.println("#######################################################################################");
		System.out.println("Inside doFilterInternal");
		System.out.println("#######################################################################################");
		*/
		String header = request.getHeader(HEADER_STRING);
		/*
		System.out.println("***************************************************************************************");
		System.out.println(header);
		System.out.println("***************************************************************************************");
		*/
		if(header == null || !header.startsWith(TOKEN_PREFIX)) {
		/*
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println("failed ");
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		*/	
			chain.doFilter(request, response);
			return;
		}
		/*
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("passed");
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		*/
		
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if(token == null) return null;
		
		String username = Jwts
							.parser()
							.setSigningKey(SECRET)
							.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
							.getBody()
							.getSubject();
		UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
		return username != null ?
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) : null;
	}
}
