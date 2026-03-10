package com.librarymanagement.mapper;

import com.librarymanagement.dto.BookDto;
import com.librarymanagement.entity.BookEntity;

public class BookMapper {
    /*
  Convert BookEntity -> BookDto
  Used when sending data to client
 */
    public static BookDto toDto(BookEntity entity){
        if(entity==null){
            return null;
        }
        return new BookDto(
                entity.getBookId(),
                entity.getBookTitle(),
                entity.getGenre(),
                entity.isLent(),
                entity.getLentTo(),
                entity.getAuthor() !=null ? entity.getAuthor().getAuthorName():null
        );
    }
    /*
   Convert BookDto → BookEntity
   Used when saving data to database
  */
    public static BookEntity toEntity(BookDto toDto){
        if(toDto==null){
            return null;
        }
        BookEntity entity=new BookEntity();
        entity.setBookId(toDto.getBookId());
        entity.setBookTitle(toDto.getBookTitle());
        entity.setGenre(toDto.getGenre());
        entity.setLent(toDto.isLent());
        entity.setLentTo(toDto.getLentTo());
        return entity;
    }

}
