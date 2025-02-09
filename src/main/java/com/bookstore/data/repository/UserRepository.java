package com.bookstore.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.data.entity.UserEntity;

import java.util.Optional;

/**
 * Repo for User DB
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
    Optional<UserEntity> findByUserName(String userName);

}
