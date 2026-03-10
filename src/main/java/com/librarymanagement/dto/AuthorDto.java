package com.librarymanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDto {
    private long authorId;
    private String authorName;
    public AuthorDto(long authorId,String authorName){
        this.authorId=authorId;
        this.authorName=authorName;
    }
    public AuthorDto(){
        //default constructor
    }
}
