package com.librarymanagement.mapper;

import com.librarymanagement.*;
import com.librarymanagement.dto.AuthorDto;
import com.librarymanagement.dto.BookDto;

public class GrpcMapper {
    //convert Book Request -> Book Dto
    public static BookDto toBookDto(BookRequest request){
        BookDto dto=new BookDto();
        dto.setBookTitle(request.getTitle());
        dto.setGenre(request.getGenre());
        dto.setAuthorName(request.getAuthorName());
        return dto;
    }
    //convert book dto -> book response
    public static BookResponse toBookResponse(BookDto dto){
        return BookResponse.newBuilder()
                .setBookId(dto.getBookId())
                .setTitle(dto.getBookTitle())
                .setAuthorName(dto.getAuthorName())
                .setGenre(dto.getGenre())
                .setIsLent(dto.isLent())
                .setLentTo(dto.getLentTo() !=null ?dto.getLentTo():"")
                .build();
    }
    //convert Book dto->Lend Response
    public static LendResponse toLendResponse(BookDto dto) {
        return LendResponse.newBuilder()
                .setBookId(dto.getBookId())
                .setTitle(dto.getBookTitle())
                .setBorrowerName(dto.getLentTo())
                .setSuccess(true)
                .setMessage("Book lend successfully")
                .build();
    }

    // Convert BookDto -> ReturnResponse
    public static ReturnResponse toReturnResponse(BookDto dto) {
        return ReturnResponse.newBuilder()
                .setBookId(dto.getBookId())
                .setTitle(dto.getBookTitle())
                .setSuccess(true)
                .setMessage("Book returned successfully")
                .build();
    }

    // Convert AuthorDto -> AuthorResponse
    public static AuthorResponse toAuthorResponse(AuthorDto dto) {
        return AuthorResponse.newBuilder()
                .setName(dto.getAuthorName())
                .build();
    }

}
