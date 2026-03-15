package com.rookies5.MySpringbootLab.controller;

import com.rookies5.MySpringbootLab.entity.Book;
import com.rookies5.MySpringbootLab.exception.BusinessException;
import com.rookies5.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookRestController {
    private final BookRepository bookRepository;

    //새 도서 등록
    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    //모든 도서 조회
    @GetMapping
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    //ID로 특정 도서 조회
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return getExistBook(optionalBook);
    }

    //ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(()->
                new BusinessException("Book Not Found with ISBN:" + isbn, HttpStatus.NOT_FOUND));
    }

    //도서 정보 수정
    @PatchMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Book existBook = getExistBook(bookRepository.findById(id));
        existBook.setTitle(bookDetail.getTitle());
        existBook.setAuthor(bookDetail.getAuthor());
        existBook.setIsbn(bookDetail.getIsbn());
        existBook.setPrice(bookDetail.getPrice());
        existBook.setPublishDate(bookDetail.getPublishDate());

        return bookRepository.save(existBook);
    }

    //도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Book book = getExistBook(bookRepository.findById(id));
        bookRepository.delete(book);
        return ResponseEntity.ok("Id = " + id + "도서가 삭제되었습니다.");
    }

    //공통 getExistBook
    private static Book getExistBook(Optional<Book> optionalBook) {
        return optionalBook.orElseThrow(()->
                new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }
}
