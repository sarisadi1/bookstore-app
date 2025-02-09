package com.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * Main application class
 */
@SpringBootApplication
@ComponentScan({ "com.bookstore" })
@EnableJdbcRepositories(basePackages = "com.bookstore.data.repository")
@EnableEncryptableProperties
public class BookStoreApplication {
	/**
	 * Default constructor for main application class
	 */
	public BookStoreApplication() {

	}

	/**
	 * Main driver method
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}
}
