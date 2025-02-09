package com.bookstore.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.business.BookStoreInterface;
import com.bookstore.controllers.ControllerBase;
import com.bookstore.models.BookModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Rest API controller for book service UI
 * 
 * Reference: https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/
 */
@RestController
@RequestMapping("/api/bookservice")
@Tag(name="bookservice", description="Operations pertaining to book management in Book Store")
public class BookStoreRestController extends ControllerBase {

	private static final Logger logger = LogManager.getLogger(BookStoreRestController.class);


	@Autowired
	private BookStoreInterface bookService;

	/**
	 * Create a default RestController for BookStore
	 */
	public BookStoreRestController() {
		
	}
	
	/**
	 * GET API for all books
	 * 
	 * @return book model response or forbidden
	 */
	@GetMapping(path = "allbooks", produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary="Get list of all books", description = "Get a list of available books", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved books", 
            		content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookModel.class))
                    )),
            @ApiResponse(responseCode = "404", description = "The book list is empty", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is not authorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Error while getting resource", content = { @Content(schema = @Schema()) })
            })
	public ResponseEntity<?> getAllBooks() {

		logger.debug("Entering getAllBooks()");

		// If the user session is valid return values
		if (hasValidUserSession()) {
			logger.info("Valid user session detected.");
			try {
				var books = bookService.getAllBooks();
				if (!books.isEmpty()) {
					logger.info("Books retrieved successfully. Total books: {}", books.size());

					// If we got a list send it
					return new ResponseEntity<>(books, HttpStatus.OK);
				} else {
					// If not send not found
					logger.warn("No books found in the database.");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (Exception ex) {
				// In case of error send internal server error
				logger.error("Error occurred while fetching all books: {}", ex.getMessage(), ex);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.warn("Invalid user session. Access forbidden.");
			// Return forbidden code if not in valid session
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
	
	/**
	 * Get the book with specific id from the store
	 * @param id book id
	 * @return book or HTTP status in case of error 
	 */
	@GetMapping(path = "getbookbyid/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary="Get a book using Id.", description = "Get details of a book identified by the Id", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book details", 
            		content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookModel.class)
                    )),
            @ApiResponse(responseCode = "404", description = "The book is not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is not authorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Error while getting book", content = { @Content(schema = @Schema()) })
            })
	public ResponseEntity<?> getBooks(@Parameter(name="id", description = "Book Id to search.", example = "BK001", required = true)
	@PathVariable String id) {
		logger.debug("Entering getBooks() with ID: {}", id);
		// If the user session is valid return values
		if (hasValidUserSession()) {
			logger.info("Valid user session detected.");
			try {
				var book = bookService.getBookForId(id, loggedInSession.getUserName());
				if (book != null) {
					logger.info("Book retrieved successfully for ID: {}", id);
					// If we got a list send it
					return new ResponseEntity<>(book, HttpStatus.OK);
				} else {
					logger.warn("Book not found for ID: {}", id);

					// If not send not found
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (Exception ex) {
				// In case of error send internal server error
				logger.error("Error occurred while fetching book with ID: {}: {}", id, ex.getMessage(), ex);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			// Return forbidden code if not in valid session
			logger.warn("Invalid user session. Access forbidden.");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
}
