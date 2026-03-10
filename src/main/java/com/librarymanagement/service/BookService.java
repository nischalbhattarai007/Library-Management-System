package com.librarymanagement.service;

import com.librarymanagement.dao.AuthorRepo;
import com.librarymanagement.dao.BookRepo;
import com.librarymanagement.dto.AuthorDto;
import com.librarymanagement.dto.BookDto;
import com.librarymanagement.entity.AuthorEntity;
import com.librarymanagement.entity.BookEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class BookService {

    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;
    @Inject
    public BookService(BookRepo bookRepo, AuthorRepo authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo=authorRepo;

    }
    public BookDto addBook(BookDto bookDto){

        AuthorEntity author = authorRepo
                .findByAuthorName(bookDto.getAuthorName())
                .orElseGet(() -> {
                    AuthorEntity newAuthor = new AuthorEntity();
                    newAuthor.setAuthorName(bookDto.getAuthorName());
                    return authorRepo.save(newAuthor);
                });

        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookTitle(bookDto.getBookTitle());
        bookEntity.setGenre(bookDto.getGenre());
        bookEntity.setLent(false);
        bookEntity.setLentTo(null);
        bookEntity.setAuthor(author);
        bookEntity = bookRepo.save(bookEntity);

        return new BookDto(
                bookEntity.getBookId(),
                bookEntity.getBookTitle(),
                bookEntity.getGenre(),
                bookEntity.isLent(),
                bookEntity.getLentTo(),
                bookEntity.getAuthor().getAuthorName()
        );
    }
    public BookDto getBookById(long bookNumber){
        Optional<BookEntity> entityOpt=bookRepo.findById((int) bookNumber);
        if(entityOpt.isEmpty()){
            return null;
        }
        BookEntity entity=entityOpt.get();
        return new BookDto(
                entity.getBookId(),
                entity.getBookTitle(),
                entity.getGenre(),
                entity.isLent(),
                entity.getLentTo(),
                entity.getAuthor().getAuthorName()
        );
    }
    public List<BookDto> listBooks(){
       return bookRepo.findAll()
               .stream()
               .map(entity->new BookDto(
                       entity.getBookId(),
                       entity.getBookTitle(),
                       entity.getGenre(),
                       entity.isLent(),
                       entity.getLentTo(),
                       entity.getAuthor().getAuthorName()
               )).toList();
    }
    public BookDto lendBook(long bookId, String borrowerName) {

        //  Load the managed book entity
        BookEntity book = bookRepo.findById((int) bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        //  Check if already lent
        if (book.isLent()) {
            throw new RuntimeException("Book is already lent to " + book.getLentTo());
        }

        //  Update lending info only — DO NOT touch author
        book.setLent(true);
        book.setLentTo(borrowerName);

        //  Save updated entity
        book = bookRepo.update(book); // use update() if available in Micronaut Data
        // or just bookRepo.save(book) works if entity is managed

        return new BookDto(
                book.getBookId(),
                book.getBookTitle(),
                book.getGenre(),
                book.isLent(),
                book.getLentTo(),
                book.getAuthor() != null ? book.getAuthor().getAuthorName() : null
        );
    }
    public BookDto returnBook(long bookId){
        BookEntity book=bookRepo.findById((int) bookId)
                .orElseThrow(()->new RuntimeException("book not found"));
        if(!book.isLent()){
            throw new RuntimeException(" Book " + book.getBookTitle()  +  " is not currently lend");
        }
        book.setLent(false);
        book.setLentTo(null);
        book=bookRepo.update(book);
        return new BookDto(
                book.getBookId(),
                book.getBookTitle(),
                book.getGenre(),
                book.isLent(),
                book.getLentTo(),
                book.getAuthor() !=null ? book.getAuthor().getAuthorName():null
        );
    }
    public List<AuthorDto> listAuthors(){
        return authorRepo.findAll()
                .stream()
                .map(author->new AuthorDto(author.getAuthorId(),author.getAuthorName()))
                .collect(Collectors.toList());
    }
    public void deleteBooks(int bookId){
       if(!bookRepo.existsById(bookId)){
           throw new RuntimeException("Book not found with this id : " + bookId);
       }
       bookRepo.deleteById(bookId);
    }
}
