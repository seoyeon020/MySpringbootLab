package com.rookies5.MySpringbootLab.dto;

import com.rookies5.MySpringbootLab.entity.Book;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수 입력 사항입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력 사항입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수 입력 사항입니다.")
        @Size(min = 10, max = 13, message = "ISBN은 10~13자여야 합니다.")
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        private Integer price;

        @PastOrPresent(message = "출판일은 오늘 또는 과거 날짜여야 합니다.")
        private LocalDate publishDate;

        //DTO를 엔티티로 변환
        public Book toEntity() {
            Book book = new Book();
            book.setTitle(this.title);
            book.setAuthor(this.author);
            book.setIsbn(this.isbn);
            book.setPrice(this.price);
            book.setPublishDate(this.publishDate);
            return book;
        }
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookUpdateRequest {
        private String title;
        private String author;

        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        private Integer price;

        private LocalDate publishDate;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        public static BookResponse from(Book book) {
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .build();
        }
    }

}
