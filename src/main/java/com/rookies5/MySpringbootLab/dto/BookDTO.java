package com.rookies5.MySpringbootLab.dto;

import com.rookies5.MySpringbootLab.entity.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class BookDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "도서 제목은 필수 입력 사항입니다.")
        private String title;

        @NotBlank(message = "저자 이름은 필수 입력 사항입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수 입력 사항입니다.")
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN 형식이 올바르지 않습니다. (하이픈 포함/미포함 10자리 또는 13자리 숫자)")
        private String isbn;

        @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
        private Integer price;

        @Past(message = "출판일은 과거 날짜여야 합니다.")
        private LocalDate publishDate;

        @Valid
        private BookDetailDTO detailRequest;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailDTO {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        public static Response fromEntity(Book book) {
            BookDetailResponse detailResponse = book.getBookDetail() != null
                    ? BookDetailResponse.builder()
                    .id(book.getBookDetail().getId())
                    .description(book.getBookDetail().getDescription())
                    .language(book.getBookDetail().getLanguage())
                    .pageCount(book.getBookDetail().getPageCount())
                    .publisher(book.getBookDetail().getPublisher())
                    .coverImageUrl(book.getBookDetail().getCoverImageUrl())
                    .edition(book.getBookDetail().getEdition())
                    .build()
                    : null;

            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailResponse)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    // Book 부분 수정용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchRequest {
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailPatchRequest detailRequest; // 중첩 구조
    }

    // BookDetail 부분 수정용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailPatchRequest {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}
