package com.bookstore.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.business.BookStoreInterface;
import com.bookstore.models.BookModel;

import jakarta.validation.Valid;

/**
 * Book management page request handling class.
 */
@Controller
@RequestMapping("/books")
public class BooksController extends ControllerBase {
	private static final Logger logger = LogManager.getLogger(BooksController.class);

	@Autowired()
	private BookStoreInterface bookService;

	/**
	 * Create book controller
	 */
	public BooksController() {
		super();
	}
	
	/**
	 * All books purchasable to the user.
	 * 
	 * @param model model
	 * @return allbooks page
	 */
	@GetMapping("/buybooks")
	public String displayBuyBooks(Model model) {
		logger.info("Displaying books available for purchase.");

		// If the user session is valid add attributes
		if (hasValidUserSession()) {
			addCommonAttributes(model);
			var lstBooks = bookService.getBooksOfOthers(loggedInSession.getUserName());
			model.addAttribute("bookList", lstBooks);
			logger.info("Books available for purchase added to model.");
		}
		else {
			logger.warn("User session is invalid, redirecting to login.");
		}
		return "buybooks";
	}
	
	/**
	 * Books listed by the user
	 * @param model model
	 * @return mybooks view
	 */
	@GetMapping("mybooks")
	public String displayBooksForUser(Model model) {
		logger.info("Displaying books listed by the current user.");
		// If the user session is valid add attributes
		if (hasValidUserSession()) {
			addCommonAttributes(model);
			var lstBooks = bookService.getBooksForUser(loggedInSession.getUserName());
			model.addAttribute("bookList", lstBooks);
			logger.info("Books listed by the user added to model.");
		} else {
			logger.warn("User session is invalid, redirecting to login.");
		}
		return "mybooks";
	}
	
	/**
	 * Handles the add book page request. 
	 * @param model model
	 * @return view name
	 */
	@GetMapping("addbook")
	public String displayAddBook(Model model) {
		logger.info("Displaying add book form.");

		// If the user session is valid add attributes
		if (hasValidUserSession()) {
			// otherwise show add form
			addCommonAttributes(model);
			var book = new BookModel();
			model.addAttribute("bookModel", book);
		}
			return "addbook";
	}

	/**
	 * Add book form submit handler. Validate for errors and add book to store
	 * 
	 * @param bookModel model
	 * @param result binding result
	 * @param model model
	 * @return view name
	 */
	@PostMapping("/doAddBook")
	public String doAddBook(@ModelAttribute @Valid BookModel bookModel, BindingResult result, Model model) {

		logger.info("Attempting to add a new book.");

		// If the user session is valid security redirects to login
		if (!hasValidUserSession()) {
			logger.warn("User session is invalid, redirecting to login.");
			return "addbook";
		}

		// otherwise process add
		addCommonAttributes(model);
		// check if there is errors
		if (result.hasErrors()) {
			logger.error("Form submission has errors.");
			return "addbook";
		}

		// If validation is success add the user
		bookModel.setOwner(loggedInSession.getUserName()); // Book is added by current user
		
		// If all looks OK. add the book to store
		if (bookService.addBook(bookModel)) {
			loggedInSession.updateUserDashboard();
			logger.info("Book added successfully.");
			// When add book succeeds, we can redirect to dashboard
			return "redirect:/dashboard";
		} else {
			result.rejectValue("name", "error.bookModel", "Error adding book. Please try again later.");
			logger.error("Error adding book to the store.");
			return "addbook";
		}
	}
	
	/**
	 * Handles the edit book page request. 
	 * @param id book id
	 * @param model model
	 * @return editbook view
	 */
	@GetMapping("editbook")
	public String displayEditBook(@RequestParam(required = false) String id, Model model) {
		logger.info("Displaying edit book form for book ID: {}", id);

		// If the user session is valid add attributes
		if (hasValidUserSession()) {
			// show add form
			addCommonAttributes(model);
			
			var book = bookService.getBookForId(id, loggedInSession.getUserName());
			if(book != null) {
				model.addAttribute("bookModel", book);
				return "editbook";
			} else {
				// Book is not found redirect to book list
				logger.warn("Book with ID: {} not found, redirecting to book list.", id);
				return "redirect:/books/mybooks";
			}
		}

		logger.warn("User session is invalid, redirecting to login.");
		return "editbook";
	}

	/**
	 * Update book form submit handler. Validate for errors and update book to store
	 * 
	 * @param bookModel book
	 * @param result result
	 * @param model model
	 * @return view name
	 */
	@PostMapping("/doEditBook")
	public String doEditBook(@ModelAttribute @Valid BookModel bookModel, BindingResult result, Model model) {

		logger.info("Attempting to update book with ID: {}", bookModel.getBookId());

		// If the user session is valid security redirects to login
		if (!hasValidUserSession()) {
			logger.warn("User session is invalid, redirecting to login.");
			return "editbook";
		}

		// otherwise process add
		addCommonAttributes(model);
		// check if there is errors
		if (result.hasErrors()) {
			logger.error("Form submission has errors.");
			return "editbook";
		}
		
		// If validation is success add the user
		bookModel.setOwner(loggedInSession.getUserName()); // Book is added by current user
		
		// If all looks OK. add the book to store
		if (bookService.updateBook(bookModel)) {
			loggedInSession.updateUserDashboard();
			// When add book succeeds, we can redirect to my books
			logger.info("Book updated successfully.");
			return "redirect:/books/mybooks";
		} else {
			result.rejectValue("name", "error.bookModel", "Error updating book. Please try again later.");
			logger.error("Error updating book in the store.");
			return "editbook";
		}
	}
	
	/**
	 * Handles the delete book page request. 
	 * @param id book id
	 * @param model model
	 * @return view name
	 */
	@GetMapping("deletebook")
	public String displayDeleteBook(@RequestParam(required = false) String id, Model model) {

		logger.info("Displaying delete book form for book ID: {}", id);

		// If the user session is valid add attributes
		if (hasValidUserSession()) {
			// show add form
			addCommonAttributes(model);
			
			var book = bookService.getBookForId(id, loggedInSession.getUserName());
			if(book != null) {
				model.addAttribute("bookModel", book);
				return "deletebook";
			} else {
				 // Go to book list if not found
				logger.warn("Book with ID: {} not found, redirecting to book list.", id);
				return "redirect:/books/mybooks";
			}
		}

		logger.warn("User session is invalid, redirecting to login.");
		return "deletebook";
	}

	/**
	 * Delete book form submit handler. Validate for errors and delete book to store
	 * 
	 * @param bookModel book
	 * @param result binding result
	 * @param model model
	 * @return view name
	 */
	@PostMapping("/doDeleteBook")
	public String doDeleteBook(@ModelAttribute @Valid BookModel bookModel, BindingResult result, Model model) {

		logger.info("Attempting to delete book with ID: {}", bookModel.getBookId());

		// If the user session is valid security redirects to login
		if (!hasValidUserSession()) {
			logger.warn("User session is invalid, redirecting to login.");
			return "deletebook";
		}

		// otherwise process add
		addCommonAttributes(model);
		// check if there is errors
		if (result.hasErrors()) {
			logger.error("Form submission has errors.");
			return "deletebook";
		}
		
		// If validation is success add the user
		bookModel.setOwner(loggedInSession.getUserName()); // Book is added by current user
		
		// If all looks OK. add the book to store
		if (bookService.deleteBook(bookModel)) {
			loggedInSession.updateUserDashboard();
			// When add book succeeds, we can redirect to my books
			logger.info("Book deleted successfully.");
			return "redirect:/books/mybooks";
		} else {
			result.rejectValue("name", "error.bookModel", "Error deleting book. Please try again later.");
			logger.error("Error deleting book from the store.");
			return "deletebook";
		}
	}
}
