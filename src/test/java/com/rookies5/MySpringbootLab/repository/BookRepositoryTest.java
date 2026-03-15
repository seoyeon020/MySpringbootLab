package com.rookies5.MySpringbootLab.repository;

import com.rookies5.MySpringbootLab.entity.Book;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    //도서 등록 테스트
    @Test
    @Rollback(value = false)
    @Disabled
    void testCreateBook() {
        //첫번째 book
        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("9788956746425");
        book.setPrice(30000);
        book.setPublishDate(LocalDate.of(2025, 5, 7));

        Book savedBook = bookRepository.save(book);
        assertThat(savedBook.getId()).isNotNull();

        //두번째 book
        Book book2 = new Book();
        book2.setTitle("JPA 프로그래밍");
        book2.setAuthor("박둘리");
        book2.setIsbn("9788956746432");
        book2.setPrice(35000);
        book2.setPublishDate(LocalDate.of(2025, 4, 30));

        Book savedBook2 = bookRepository.save(book2);
        assertThat(savedBook2.getId()).isNotNull();

    }

    //ISBN으로 도서 조회 테스트
    @Test
    void testFindByIsbn() {
        Optional<Book> found = bookRepository.findByIsbn("9788956746432");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("JPA 프로그래밍");
        assertThat((found.get().getAuthor())).isEqualTo("박둘리");
    }

    //저자명으로 도서목록 조회 테스트
    @Test
    void testFindByAuthor() {
        List<Book> books = bookRepository.findByAuthor("홍길동");

        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getTitle()).isEqualTo("스프링 부트 입문");
    }

    //도서 정보 수정 테스트
    @Test
    @Rollback(value = false)
    void testUpdateBook() {
        Optional<Book> found = bookRepository.findByIsbn("9788956746425");
        assertThat(found).isPresent();

        Book book = found.get();
        book.setPrice(32000);
        Book updatedBook = bookRepository.save(book);

        assertThat(updatedBook.getPrice()).isEqualTo(32000);
    }

    //도서 삭제 테스트
    @Test
    @Rollback(value = false)
    void testDeleteBook() {
        Optional<Book> found = bookRepository.findByIsbn("9788956746432");
        assertThat(found).isPresent();

        bookRepository.delete(found.get());

        Optional<Book> deletedBook = bookRepository.findByIsbn("9788956746432");
        assertThat(deletedBook).isEmpty();
    }
}
