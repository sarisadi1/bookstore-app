package com.bookstore.configs;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Class to control Spring security configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private static final Logger logger = LogManager.getLogger(SecurityConfiguration.class);
	
	@Autowired
	private UserDetailsService service; // User management for lookup
	
	// Encryption object
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	/**
	 * Create security config object
	 */
	public SecurityConfiguration() {
		
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	/**
	 * Configures the security filter chain for Form based HTTP authentication.
	 * @param http security object
	 * @return filter chain
	 * @throws Exception error
	 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("Custom Security Configuration is applied!");
        System.out.println("Custom Security Configuration is applied!");

        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> requests
                .requestMatchers("/","/images/**","/styles/**", "/signUp/**", "/about").permitAll() // Avoid home, images, and signUp pages
                .anyRequest().authenticated())
        
                .formLogin(login -> login.loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                        .failureUrl("/login-error")
                        .defaultSuccessUrl("/login-success", true)) // Redirect to login success
               .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                        .logoutSuccessUrl("/"));
       
      return http.build();
    }
    
    /**
     * Set the service validate user details and password encoder to use.
     * @param auth authentication
     * @throws Exception error
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(service)
              .passwordEncoder(encoder);
    }

	@PostConstruct
	public void logSecuritySetup() {
		logger.info("Spring Security configuration initialized successfully.");
	}
}