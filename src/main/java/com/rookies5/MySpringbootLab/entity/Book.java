package com.rookies5.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    private Integer price;
    private LocalDate publishDate;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookDetail bookDetail;

    public void setBookDetail(BookDetail bookDetail) {
        this.bookDetail = bookDetail;
        if (bookDetail != null) {
            bookDetail.setBook(this);
        }
    }


}
