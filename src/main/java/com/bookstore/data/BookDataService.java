package com.bookstore.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.data.entity.BookEntity;
import com.bookstore.data.repository.BookRepository;

/**
 * Book data repo management class
 */
@Service("BookDataService")
public class BookDataService implements DataAccessInterface<BookEntity> {

	private static final Logger logger = LogManager.getLogger(BookDataService.class);

	@Autowired
	private BookRepository bookRepo;

	/**
	 * Construct Book Data Service
	 */
	public BookDataService() {
		
	}
	
	/**
	 * Find all books
	 */
	@Override
	public List<BookEntity> findAll() {
		var books = new ArrayList<BookEntity>();
		try {
			var iter = bookRepo.findAll();
			iter.forEach(books::add);
			logger.info("Fetched all books, total count: {}", books.size());
		} catch (Exception ex) {
			logger.error("Error occurred while fetching all books", ex);
			ex.printStackTrace();
		}

		return books;
	}

	
	/**
	 * Find book for id
	 */
	@Override
	public BookEntity findById(long id) {
		try {
			var book = bookRepo.findById(id);
			if (book.isPresent()) {
				logger.info("Book found with ID: {}", id);
				return book.get();
			} else {
				logger.warn("No book found with ID: {}", id);
			}
		} catch (Exception ex) {
			logger.error("Error occurred while fetching book with ID: {}", id, ex);
		}

		return null;
	}

	/**
	 * create new book
	 */
	@Override
	public BookEntity create(BookEntity book) {
		try {
			BookEntity savedBook = bookRepo.save(book);
			logger.info("Successfully created new book: {}", book.getName());
			return savedBook;
		} catch (Exception ex) {
			logger.error("Error occurred while creating book: {}", book.getName(), ex);
		}
		return null;
	}

	/**
	 * update existing one
	 */
	@Override
	public boolean update(BookEntity book) {
		try {
			bookRepo.save(book);
			logger.info("Successfully updated book: {}", book.getName());
		} catch (Exception ex) {
			logger.error("Error occurred while updating book: {}", book.getName(), ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * delete a book
	 */
	@Override
	public boolean delete(BookEntity book) {
		try {
			bookRepo.delete(book);
			logger.info("Successfully deleted book: {}", book.getName());
		} catch (Exception ex) {
			logger.error("Error occurred while deleting book: {}", book.getName(), ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
