package com.librarymanagement.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Serdeable
@Entity
@Table(name = "Authors")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long authorId;
    @NotBlank(message = "Author name should not be empty")
    @Size(message = "Author name should be more than 30 characters", max = 30)
    private String authorName;
    @OneToMany(mappedBy = "author")
    private List<BookEntity> books;
    public AuthorEntity(long authorId,String authorName){
        this.authorId=authorId;
        this.authorName=authorName;
    }
    public AuthorEntity(){
        //default constructor
    }
}
