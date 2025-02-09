package com.bookstore.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.data.entity.BookEntity;

/**
 * Repo for Book DB
 */
@Repository
public interface BookRepository extends CrudRepository<BookEntity, Long>{

}
