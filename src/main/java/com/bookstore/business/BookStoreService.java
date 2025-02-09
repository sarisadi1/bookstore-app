package com.bookstore.business;

import java.util.ArrayList;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.data.BookDataService;
import com.bookstore.data.UserDataService;
import com.bookstore.data.entity.BookEntity;
import com.bookstore.data.entity.UserEntity;
import com.bookstore.models.BookModel;
/**
 * Main Book service class
 */
@Service
public class BookStoreService implements BookStoreInterface {

	private static final Logger logger = LogManager.getLogger(BookStoreService.class);

	@Autowired
	private BookDataService bookService;

	@Autowired
	private UserDataService userService;

	/**
	 * Create a BookStore Service
	 */
	public BookStoreService() {
		
	}
	/**
	 * Create BookModel object from entity
	 * 
	 * @param book entity
	 * @return model
	 */
	private BookModel entityToModel(BookEntity book, String user) {
		logger.debug("Converting BookEntity to BookModel for book: {}", book.getName());
		return new BookModel(book.getBookId(), book.getName(), book.getAuthor(), book.getPublishDate(),
				book.getDescription(), book.getPrice(), book.getQuantity(), user);
	}

	/**
	 * Create entity based on model
	 * 
	 * @param book model
	 * @return entity 
	 */
	private BookEntity modelToEntity(BookModel book) {
		logger.debug("Converting BookModel to BookEntity for book: {}", book.getName());
		// Find the owner id
		var owner = findUserEntity(book.getOwner());
		if (owner != null) {
			try {
				// Parse Book ID to number by removing BK part
				var longId = Integer.parseInt(book.getBookId().substring(2));
				// Create entity
				return new BookEntity(longId, book.getBookId(), book.getName(), book.getAuthor(), book.getPublishDate(),
						book.getDescription(), book.getPrice(), book.getQuantity(), owner.getId());
			} catch (Exception ex) {
				logger.error("Error converting BookModel to BookEntity: {}", ex.getMessage(), ex);
				ex.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Get all books in the DB
	 */
	@Override
	public List<BookModel> getAllBooks() {
		logger.info("Fetching all books");
		var books = bookService.findAll();

		var bookModels = new ArrayList<BookModel>();
		for (var book : books) {

			var owner = userService.findById(book.getOwner());
			if (owner != null) {
				bookModels.add(entityToModel(book, owner.getUserName()));
			}
		}

		logger.info("Fetched {} books", bookModels.size());
		return bookModels;
	}

	/**
	 * Get the books for a user based on name
	 */
	@Override
	public List<BookModel> getBooksForUser(String userName) {

		// TODO: Update UserDataService to use custom query to find user with WHERE
		// clause
		logger.info("Fetching books for user: {}", userName);
		UserEntity userObj = findUserEntity(userName);

		var bookModels = new ArrayList<BookModel>();
		if (userObj != null) {
			var books = bookService.findAll();
			for (var book : books) {

				if (book.getOwner() == userObj.getId()) {
					bookModels.add(entityToModel(book, userObj.getUserName()));
				}
			}
		}

		logger.info("Fetched {} books for user: {}", bookModels.size(), userName);
		return bookModels;
	}

	/**
	 * Find a user entity based on name
	 * 
	 * @param userName
	 * @return
	 */
	private UserEntity findUserEntity(String userName) {
		logger.debug("Finding user entity for userName: {}", userName);
		UserEntity userObj = null;

		// Get all users and compare names
		var users = userService.findAll();
		for (var user : users) {
			if (user.getUserName().compareTo(userName) == 0) {
				userObj = user;
			}
		}

		logger.warn("User not found for userName: {}", userName);
		return userObj;
	}

	/**
	 * Add new book
	 */
	@Override
	public boolean addBook(BookModel book) {
		logger.info("Adding book: {}", book.getName());
		// Find the owner id
		var owner = findUserEntity(book.getOwner());
		if (owner != null) {
			// Create entity and update DB with temp BOOK_ID we will update it below
			var entity = new BookEntity(0, "BK1", book.getName(), book.getAuthor(), book.getPublishDate(),
					book.getDescription(), book.getPrice(), book.getQuantity(), owner.getId());
			entity = bookService.create(entity); // Get updated entity

			if (entity != null) {
				// TODO: Workaround : update book id with the new id from DB
				entity.setBookId(String.format("BK%d", entity.getId()));
				logger.info("Book added successfully: {}", book.getName());
				return bookService.update(entity);
			}
		}

		logger.warn("Failed to add book: {}", book.getName());
		return false;
	}

	/**
	 * Update a book
	 */
	@Override
	public boolean updateBook(BookModel book) {
		// Create entity and update DB
		logger.info("Updating book: {}", book.getName());
		var entity = modelToEntity(book);
		if (entity != null) {
			logger.info("Book update status: {}", bookService.update(entity));
			return bookService.update(entity);
		}

		logger.warn("Failed to update book: {}", book.getName());
		return false;
	}

	/**
	 * Delete book
	 */
	@Override
	public boolean deleteBook(BookModel book) {

		// Create entity and delete from DB
		logger.info("Deleting book: {}", book.getName());
		var entity = modelToEntity(book);
		if (entity != null) {
			boolean success = bookService.delete(entity);
			logger.info("Book delete status: {}", success);
			return success;
		}
		logger.warn("Failed to delete book: {}", book.getName());
		return false;
	}

	/**
	 * Get book with a specific id
	 */
	@Override
	public BookModel getBookForId(String id, String userName) {
		logger.info("Fetching book for id: {}", id);
		try {
			// Parse Book ID to number by removing BK part
			var longId = Integer.parseInt(id.substring(2));
			var book = bookService.findById(longId);
			if (book != null) {
				UserEntity userObj = findUserEntity(userName);
				return entityToModel(book, userObj.getUserName());
			}
		} catch (Exception ex) {
			logger.error("Error fetching book for id {}: {}", id, ex.getMessage(), ex);
			ex.printStackTrace();
		}
		logger.warn("No book found for id: {}", id);
		return null;
	}

	@Override
	public boolean deleteAllBooksForUser(String userName) {
		logger.info("Deleting all books for user: {}", userName);
		UserEntity userObj = findUserEntity(userName);
		if (userObj != null) {
			var books = bookService.findAll();
			for (var book : books) {

				if (book.getOwner() == userObj.getId()) {
					bookService.delete(book);
				}
			}
		}

		logger.info("All books deleted for user: {}", userName);
		return true;
	}

	@Override
	public List<BookModel> getBooksOfOthers(String currentUser) {
		// TODO: Update UserDataService to use custom query to find user with WHERE
		// clause
		logger.info("Fetching books of others for current user: {}", currentUser);
		UserEntity userObj = findUserEntity(currentUser);

		var bookModels = new ArrayList<BookModel>();
		if (userObj != null) {
			var books = bookService.findAll();
			for (var book : books) {

				if (book.getOwner() != userObj.getId()) {
					bookModels.add(entityToModel(book, userObj.getUserName()));
				}
			}
		}

		logger.info("Fetched {} books of others for user: {}", bookModels.size(), currentUser);
		return bookModels;
	}
}
