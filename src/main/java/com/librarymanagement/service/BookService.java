package com.librarymanagement.service;

import com.librarymanagement.dao.AuthorRepo;
import com.librarymanagement.dao.BookRepo;
import com.librarymanagement.dto.AuthorDto;
import com.librarymanagement.dto.BookDto;
import com.librarymanagement.entity.AuthorEntity;
import com.librarymanagement.entity.BookEntity;
import com.librarymanagement.exception.BookAlreadyLentException;
import com.librarymanagement.exception.BookNotFoundException;
import com.librarymanagement.exception.NameNotFoundException;
import com.librarymanagement.mapper.AuthorMapper;
import com.librarymanagement.mapper.BookMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;
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
        //author entity fetched form DB and then inject into entity
        AuthorEntity author = authorRepo
                .findByAuthorName(bookDto.getAuthorName())
                .orElseGet(() -> {
                    AuthorEntity newAuthor = new AuthorEntity();
                    newAuthor.setAuthorName(bookDto.getAuthorName());
                    return authorRepo.save(newAuthor);
                });
        BookEntity bookEntity= BookMapper.toEntity(bookDto);
        bookEntity.setLent(false);
        bookEntity.setLentTo(null);
        bookEntity.setAuthor(author);
        bookEntity = bookRepo.save(bookEntity);
        return BookMapper.toDto(bookEntity);
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
                .orElseThrow(() -> new BookNotFoundException("Book not found with this id : " + bookId));
        if(borrowerName.isEmpty()){
            throw new NameNotFoundException("Borrower Name must be entered");
        }
        //  Check if already lent
        if (book.isLent()) {
            throw new BookAlreadyLentException("Book is already lent to " + book.getLentTo());
        }

        book.setLent(true);
        book.setLentTo(borrowerName);

        book = bookRepo.update(book);

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
                .orElseThrow(()->new BookNotFoundException("book not found"));
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
                .map(AuthorMapper::toDto)
                .toList();
    }
    public void deleteBooks(int bookId){
       if(!bookRepo.existsById(bookId)){
           throw new BookNotFoundException("Book not found with this id : " + bookId);
       }
       bookRepo.deleteById(bookId);
    }
    public List<BookDto> searchBooks(String query){
        List<BookEntity> books=bookRepo.findByBookTitleContainingIgnoreCaseOrAuthor_AuthorNameContainingIgnoreCase(query,query);
        return books.stream()
                .map(bookEntity -> new BookDto(
                        bookEntity.getBookId(),
                        bookEntity.getBookTitle(),
                        bookEntity.getGenre(),
                        bookEntity.isLent(),
                        bookEntity.getLentTo(),
                        bookEntity.getAuthor().getAuthorName()
                ))
                .toList();
    }

}
