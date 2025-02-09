package com.bookstore.business;

import java.util.List;

import com.bookstore.models.BookModel;

/**
 * Book Store Business interface
 */
public interface BookStoreInterface {
	
	/**
	 * Get all the books 
	 * @return books
	 */
	public List<BookModel> getAllBooks();
	
	/**
	 * Get books listed by a user
	 * @param user user
	 * @return books
	 */
	public List<BookModel> getBooksForUser(String user);
	
	/**
	 * Get Book data for a specific id of book
	 * @param id book id
	 * @param userName user listed
	 * @return book model
	 */
	public BookModel getBookForId(String id, String userName);
	
	/**
	 * Adds a book to the store 
	 * @param book book
	 * @return true/false
	 */
	public boolean addBook(BookModel book);
	
	/**
	 * Update details of a book with new data 
	 * @param book book
	 * @return true/false
	 */
	public boolean updateBook(BookModel book);
	
	/**
	 * Remove a book from store
	 * @param book book 
	 * @return true/false
	 */
	public boolean deleteBook(BookModel book);
	
	/**
	 * Delete all books for the user
	 * @param user user
	 * @return success or fail
	 */
	public boolean deleteAllBooksForUser(String user);
	
	/**
	 * Get the list of book that belong to other users than currently logged in.
	 * 
	 * @param currentUser current user
	 * @return list of books
	 */
	public List<BookModel> getBooksOfOthers(String currentUser);
}
