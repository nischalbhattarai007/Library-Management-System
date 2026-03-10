package com.librarymanagement.dao;

import com.librarymanagement.entity.BookEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface BookRepo extends CrudRepository<BookEntity,Integer> {
    List<BookEntity> findByBookTitleContainingIgnoreCaseOrAuthor_AuthorNameContainingIgnoreCase(String bookTitle, String authorName);
}
