package com.rookies5.MySpringbootLab.controller;

import com.rookies5.MySpringbootLab.dto.BookDTO;
import com.rookies5.MySpringbootLab.entity.Book;
import com.rookies5.MySpringbootLab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //새 도서 등록
    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response response = bookService.createBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //모든 도서 목록 조회
    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    //ID로 특정 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    //ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    //저자로 도서 조회
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> getBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    //제목으로 도서 조회
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> getBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    //도서 전체 수정
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(@PathVariable Long id,
                                                           @Valid @RequestBody BookDTO.Request request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    //도서 부분 수정
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.Response> patchBook(@PathVariable Long id,
                                                      @RequestBody BookDTO.PatchRequest request) {
        return ResponseEntity.ok(bookService.patchBook(id, request));
    }

    //도서 상세 정보만 부분 수정
    @PatchMapping("/{id}/detail")
    public ResponseEntity<BookDTO.Response> patchBookDetail(@PathVariable Long id,
                                                            @RequestBody BookDTO.BookDetailPatchRequest request) {
        return ResponseEntity.ok(bookService.patchBookDetail(id, request));
    }

    //도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully!");
    }
}
