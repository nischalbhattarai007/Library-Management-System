package com.librarymanagement.dao;

import com.librarymanagement.entity.AuthorEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface AuthorRepo extends CrudRepository<AuthorEntity,Integer> {
    Optional<AuthorEntity> findByAuthorName(String authorName);
}
