package com.librarymanagement.dao;

import com.librarymanagement.entity.BookEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
@Repository
public interface BookRepo extends CrudRepository<BookEntity,Integer> {
}
