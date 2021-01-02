package com.seeth.config.security;

/**
 * 
 * 
 * Constants used to create JSON  web tokens
 *
 *
 * @author Seethend Reddy D
 * <br>
 * 03-Dec-2018 7:09:01 PM
 *
 */
public class SecurityConstants {
	
	/**
	 * Header key for every request JSON token
	 */
	public static final String HEADER_STRING = "Authorization";
	
	/**
	 * Prefix for every JSON token
	 */
	public static final String TOKEN_PREFIX = "cmps ";
	
	/**
	 * Secret to encode and decode the JSON token
	 */
	public static final String SECRET = "cmps";
	
	/**
	 * Sets the expiry time for JSON web tokens
	 */
	public static final long EXPIRATION_TIME = 3600000L; //5mins
	
}
