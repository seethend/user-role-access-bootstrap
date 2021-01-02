package com.seeth.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.seeth.user.personal.services.UserService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaRepositories("com.seeth")
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(encoder());
	}

	/*
	 * private PasswordEncoder getPasswordEncoder() { return new PasswordEncoder() {
	 * 
	 * @Override public boolean matches(CharSequence charSequence, String string) {
	 * return charSequence.equals(string); }
	 * 
	 * @Override public String encode(CharSequence charSequence) { return
	 * charSequence.toString(); } }; }
	 */
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder(10);
	}
	
	/**
	 * 
	 * In this method we configure the url patterns to add security to them
	 * 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
//		.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
        .and().csrf().disable()
        .authorizeRequests()
        
        // These don't have any authentication required. These are required to load the angular files
        .antMatchers("/index.html", "/", "/main.js", "/polyfills.js", "/runtime.js", "/styles.js", "/vendor.js").permitAll()
        
        // This url is used to save user while signup
        .antMatchers(HttpMethod.POST, "/save").permitAll()
        
        // Below url patterns need authentication and authorization everytime they are accessed
        .antMatchers("*/secured/*").authenticated()
        
        .anyRequest().permitAll()
        .and()
        .addFilter(new JWTAuthenticationFilter(authenticationManager()))
        .addFilter(new JWTAuthorizationFilter(authenticationManager(), userService))
        .logout();
	}

	/*
	 * @Bean public WebMvcConfigurer corsConfigurer() { return new
	 * WebMvcConfigurer() {
	 * 
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * registry.addMapping("/**").allowedOrigins("http://localhost:4200"); } }; }
	 * 
	 */	
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
