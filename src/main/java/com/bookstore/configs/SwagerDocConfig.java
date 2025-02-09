package com.bookstore.configs;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Class that configure Swagger information for API Browsing 
 * Reference: https://bell-sw.com/blog/documenting-rest-api-with-swagger-in-spring-boot-3/
 */
@Configuration
public class SwagerDocConfig {
	private static final Logger logger = LogManager.getLogger(SwagerDocConfig.class);
	
	/**
	 * Create swagger configuration object
	 */
	public SwagerDocConfig() {
		logger.info("Swagger configuration class initialized.");
	}

	/**
	 * Create Open API server and configure the details.
	 * Return a bean object for API browsing 
	 * 
	 * @return OpenAPI bean
	 */
	@Bean
	public OpenAPI defineOpenApi() {
		logger.info("Defining OpenAPI configuration...");

		// Add a server to serve Swager end point
		Server server = new Server();
		server.setUrl("http://localhost:8080");
		server.setDescription("Development");

		// Contact information
		Contact myContact = new Contact();
		myContact.setName("Book Store App Dev Team");
		myContact.setEmail("development@bookstore.com");

		// Book Store API heading and other info 
		Info information = new Info().title("Book Store API").version("1.0")
				.description("This API exposes endpoints to manage books in book store services.").contact(myContact);

		logger.info("OpenAPI configuration defined successfully with title: {} and version: {}", information.getTitle(), information.getVersion());

		return new OpenAPI().info(information).servers(List.of(server));
	}
}
