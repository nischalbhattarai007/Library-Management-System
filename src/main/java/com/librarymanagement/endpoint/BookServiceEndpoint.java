package com.librarymanagement.endpoint;

import com.librarymanagement.*;
import com.librarymanagement.dto.AuthorDto;
import com.librarymanagement.dto.BookDto;
import com.librarymanagement.service.BookService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import com.librarymanagement.mapper.GrpcMapper;
import java.util.List;

@Slf4j
@Singleton
public class BookServiceEndpoint extends LibraryServiceGrpc.LibraryServiceImplBase {
    private final BookService bookService;

    @Inject
    public BookServiceEndpoint(BookService bookService) {
        this.bookService = bookService;
    }

    // ================= addBook =================
    @Override
    public void addBook(BookRequest bookRequest, StreamObserver<BookResponse> responseObserver) {
        try {
            // Validation
            String title = bookRequest.getTitle();
            String genre = bookRequest.getGenre();
            String authorName = bookRequest.getAuthorName();
            if(authorName.isBlank()){
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Author name should not be empty")
                                .asRuntimeException()
                );
                return;
            }
            if (title.isBlank()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Book title should not be empty")
                                .asRuntimeException()
                );
                return;
            }

            if (title.length() > 50) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Book title should not exceed 50 characters")
                                .asRuntimeException()
                );
                return;
            }

            if (genre.isBlank()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Book genre should not be empty")
                                .asRuntimeException()
                );
                return;
            }

           BookDto bookDto=GrpcMapper.toBookDto(bookRequest);
            BookDto savedBook=bookService.addBook(bookDto);
            BookResponse response=GrpcMapper.toBookResponse(savedBook);

            log.info("New book '{}' added successfully", title);

            //  Send response
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error adding book", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    // ================= getBookById =================
    @Override
    public void getBookById(BookIdRequest bookIdRequest, StreamObserver<BookResponse> responseObserver) {
        try {
            // Call service to get book
            BookDto bookDto = bookService.getBookById(bookIdRequest.getBookId());

            // Check if book exists
            if (bookDto == null) {
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("Book with id " + bookIdRequest.getBookId() + " not found")
                                .asRuntimeException()
                );
                return;
            }

           BookResponse response=GrpcMapper.toBookResponse(bookDto);

            log.info("Book '{}' retrieved successfully", bookDto.getBookTitle());

            //  Send response
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error fetching book by ID", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
    @Override
    public void listBooks(Empty emp, StreamObserver<BookResponse> responseObserver) {

        try {

            List<BookDto> books = bookService.listBooks();

            if (books.isEmpty()) {
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("No books found")
                                .asRuntimeException()
                );
                return;
            }

            for (BookDto bookDto : books) {
                responseObserver.onNext(GrpcMapper.toBookResponse(bookDto));
            }

            responseObserver.onCompleted();

        } catch (Exception e) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error retrieving books: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
    @Override
    public void lendBook(LendRequest request, StreamObserver<LendResponse> responseObserver) {

        try {

            long bookId = request.getBookId();
            String borrower = request.getBorrowerName();

            if (borrower.isBlank()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Borrower name cannot be empty")
                                .asRuntimeException()
                );
                return;
            }

            BookDto lentBook = bookService.lendBook(bookId, borrower);

            LendResponse response = LendResponse.newBuilder()
                    .setBookId(lentBook.getBookId())
                    .setTitle(lentBook.getBookTitle())
                    .setBorrowerName(lentBook.getLentTo())
                    .setMessage("Book lend successfully")
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error lending book: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
    @Override
    public void returnBook(ReturnRequest returnRequest, StreamObserver<ReturnResponse> responseObserver){
        try{
            long bookId=returnRequest.getBookId();
            if(bookId<=0){
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Book id must be greater than 0")
                                .asRuntimeException()
                );
                return;
            }
            BookDto returnedBook=bookService.returnBook(bookId);
            ReturnResponse response=ReturnResponse.newBuilder()
                    .setBookId(returnedBook.getBookId())
                    .setTitle(returnedBook.getBookTitle())
                    .setSuccess(true)
                    .setMessage("Book returned successfully!")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        catch (RuntimeException e) {
            // Handle known service errors like book not found
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
        catch(Exception e){
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error returning book : " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
    @Override
public void listAuthors(Empty request,StreamObserver<AuthorResponse> responseObserver){
        try{
            List<AuthorDto> authors=bookService.listAuthors();
            if(authors.isEmpty()){
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("No author found")
                                .asRuntimeException()
                );
                return;
            }
            for(AuthorDto author:authors){
                AuthorResponse response=AuthorResponse.newBuilder()
                        .setName(author.getAuthorName())
                        .build();
                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();
        }
        catch(Exception e){
            log.error("Error listing authors",e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error"+e.getMessage())
                            .asRuntimeException()
            );
        }
}
@Override
public void deleteBooks(DeleteBookRequest request,StreamObserver<DeleteBookResponse> responseObserver){
        try{
            long bookId=request.getBookId();
            if(bookId<=0){
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Book id must be greater than 0")
                                .asRuntimeException()
                );
                return;
            }
            bookService.deleteBooks((int) bookId);
            DeleteBookResponse response=DeleteBookResponse.newBuilder()
                    .setMessage("Book deleted successfully")
                    .build();
            log.info("Book with id {} deleted successfully",bookId );
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Book not found" + e.getMessage())
                            .asRuntimeException()
            );
            return;
        }
        catch (Exception e){
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal error : " + e.getMessage())
                            .asRuntimeException()
            );
        }
}


}