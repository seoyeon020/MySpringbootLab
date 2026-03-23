package com.rookies5.MySpringbootLab.repository;

import com.rookies5.MySpringbootLab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    // ISBN으로 도서 조회
    Optional<Book> findByIsbn(String isbn);
    //저자명으로 도서 목록 조회
    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByTitleContainingIgnoreCase(String title);

    boolean existsByIsbn(String isbn);

    //상세정보까지 한번에
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);


}
