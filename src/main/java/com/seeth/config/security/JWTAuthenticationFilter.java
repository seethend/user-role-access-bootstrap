package com.seeth.config.security;

import static com.seeth.config.security.SecurityConstants.*;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeth.user.personal.models.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 *
 * Authentication filter to create JSON web tokens for new request
 * <br>
 * It will authenticate user based on username and password
 * <br>
 * When a new request without any token comes filter will call {@link #attemptAuthentication(HttpServletRequest, HttpServletResponse)} method here it will authenticate user
 * <br>
 * On successfull authentication spring will call {@link #successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication)} method to create a JSON web token add to response header
 * <br>
 * 
 * @author Seethend Reddy D
 * <br>
 * 03-Dec-2018 6:25:12 PM
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			return this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		}
		catch(IOException io) {
			throw new RuntimeException(io);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String username = authResult.getName();
		
		String token = Jwts
						.builder()
						.setSubject(username)
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS256, SECRET)
						.compact();
		String bearerToken = TOKEN_PREFIX + token;
		response.getWriter().write(bearerToken);
		response.addHeader(HEADER_STRING, bearerToken);
	}
    
    

}
