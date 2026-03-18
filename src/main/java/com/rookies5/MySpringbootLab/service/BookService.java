package com.rookies5.MySpringbootLab.service;

import com.rookies5.MySpringbootLab.dto.BookDTO;
import com.rookies5.MySpringbootLab.entity.Book;
import com.rookies5.MySpringbootLab.exception.BusinessException;
import com.rookies5.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    //모든 도서 목록 조회
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.BookResponse::from)
                .collect(Collectors.toList());
    }

    //ID로 도서 조회
    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    //ISBN으로 도서 조회
    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(()-> new BusinessException("Book Not Found with ISBN", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    //저자로 도서 조회
    public List<BookDTO.BookResponse> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream()
                .map(BookDTO.BookResponse::from)
                .collect(Collectors.toList());
    }

    //도서 등록
    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        // DTO 내부의 toEntity() 메서드를 이용해 엔티티 생성
        Book book = request.toEntity();
        Book savedBook = bookRepository.save(book);
        // 엔티티를 응답 DTO로 변환하여 반환
        return BookDTO.BookResponse.from(savedBook);
    }

    //도서 정보 수정 (입력값이 있는 경우만 업데이트)
    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        if (request.getTitle() != null) existBook.setTitle(request.getTitle());
        if (request.getAuthor() != null) existBook.setAuthor(request.getAuthor());
        if (request.getPrice() != null) existBook.setPrice(request.getPrice());
        if (request.getPublishDate() != null) existBook.setPublishDate(request.getPublishDate());

        return BookDTO.BookResponse.from(existBook);
    }

    //도서 삭제
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        bookRepository.delete(book);
    }
}
