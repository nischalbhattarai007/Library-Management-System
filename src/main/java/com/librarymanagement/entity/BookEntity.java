package com.librarymanagement.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Serdeable
@Getter
@Setter
@Table(name = "books")
@Entity
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;
    @Size(message = "Book title size should not be more than 50 characters", max = 50)
    @NotBlank(message = "Book title should not be empty")
    private String bookTitle;
    @NotBlank(message = "Book genre should not be empty")
    private String genre;
    @Column(name = "is_lent", nullable = false)
    private boolean lent = false;     // Track if the book is lent
    private String lentTo=null;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    public BookEntity(String bookTitle,String genre, boolean lent,String lentTo,AuthorEntity author){
        this.bookTitle=bookTitle;
        this.genre=genre;
        this.lent=false;
        this.lentTo=null;
        this.author = author;
    }
    public BookEntity(){
        //default constructor
    }
}

