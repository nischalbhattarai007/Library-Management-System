package com.librarymanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    private long bookId;
    private String bookTitle;
    private String genre;
    private boolean lent;
    private String lentTo;
    private String authorName;

    // Full constructor
    public BookDto(long bookId, String bookTitle, String genre, boolean lent, String lentTo,String authorName) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.genre = genre;
        this.lent = lent;
        this.lentTo = lentTo;
        this.authorName=authorName;
    }

    // Constructor used when creating a book
    public BookDto(String bookTitle, String genre, boolean lent, String lentTo,String authorName) {
        this.bookTitle = bookTitle;
        this.genre = genre;
        this.lent = lent;
        this.lentTo = lentTo;
        this.authorName=authorName;
    }

    // Default constructor
    public BookDto() {
    }
}