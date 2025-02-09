package com.bookstore.data;

import java.util.List;

/**
 * Data access template interface
 * @param <T> type
 */
public interface DataAccessInterface <T> {
	/**
	 * Find all elements
	 * @return entity
	 */
	public List<T> findAll();
	/**
	 * Find element by id
	 * @param id id
	 * @return entity
	 */
	public T findById(long id);
	
	/**
	 * Create a element
	 * @param t entity
	 * @return updated entity
	 */
	public T create(T t);
	
	/**
	 * update element 
	 * @param t entity
	 * @return success or fail
	 */
	public boolean update(T t);
	
	/**
	 * delete element
	 * @param t entity
	 * @return success or fail
	 */
	public boolean delete(T t);
}