package com.librarymanagement.mapper;

import com.librarymanagement.dto.AuthorDto;
import com.librarymanagement.entity.AuthorEntity;

public class AuthorMapper {
    public static AuthorDto toDto(AuthorEntity entity){
        if(entity==null){
            return null;
        }
        return new AuthorDto(
                entity.getAuthorId(),
                entity.getAuthorName()
        );
    }
}
