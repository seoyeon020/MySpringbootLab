package com.rookies5.MySpringbootLab.service;

import com.rookies5.MySpringbootLab.dto.BookDTO;
import com.rookies5.MySpringbootLab.entity.Book;
import com.rookies5.MySpringbootLab.entity.BookDetail;
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
    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    //ID로 도서 조회
    public BookDTO.Response getBookById(Long id) {
        return bookRepository.findByIdWithBookDetail(id).map(BookDTO.Response::fromEntity)
                .orElseThrow(()-> new BusinessException("Not Found", HttpStatus.NOT_FOUND));
    }

    //ISBN으로 도서 조회
    public BookDTO.Response getBookByIsbn(String isbn) {
        return bookRepository.findByIsbnWithBookDetail(isbn).map(BookDTO.Response::fromEntity)
                .orElseThrow(()-> new BusinessException("Not Found", HttpStatus.NOT_FOUND));
    }

    //저자로 도서 조회
    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookDTO.Response::fromEntity).collect(Collectors.toList());
    }

    //제목으로 도서 조회
    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookDTO.Response::fromEntity).collect(Collectors.toList());
    }

    //도서 등록
    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("ISBN already exists", HttpStatus.BAD_REQUEST);
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        if (request.getDetailRequest() != null) {
            BookDetail detail = BookDetail.builder()
                    .description(request.getDetailRequest().getDescription())
                    .language(request.getDetailRequest().getLanguage())
                    .pageCount(request.getDetailRequest().getPageCount())
                    .publisher(request.getDetailRequest().getPublisher())
                    .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                    .edition(request.getDetailRequest().getEdition())
                    .book(book)
                    .build();
            book.setBookDetail(detail);
        }
        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }

    //전체 수정 (PUT)
    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        return BookDTO.Response.fromEntity(book);
    }

    //PATCH 부분 수정
    //도서 정보 수정 (입력값이 있는 경우만 업데이트)
    @Transactional
    public BookDTO.Response patchBook(Long id, BookDTO.PatchRequest request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("Book Not found", HttpStatus.NOT_FOUND));

        //ISBN 체크 로직
        if (request.getIsbn() != null && !book.getIsbn().equals(request.getIsbn())) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                throw new BusinessException("ISBN Duplicate", HttpStatus.BAD_REQUEST);
            }
            book.setIsbn(request.getIsbn());
        }

        // 필드가 null이 아닐 때만 업데이트 (부분 수정)
        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getPublishDate() != null) book.setPublishDate(request.getPublishDate());

        // BookDetail 부분 수정
        if (request.getDetailRequest() != null && book.getBookDetail() != null) {
            applyDetailPatch(book.getBookDetail(), request.getDetailRequest());
        }

        return BookDTO.Response.fromEntity(book);
    }

    //BookDetail 만 따로 수정하는 로직
    @Transactional
    public BookDTO.Response patchBookDetail(Long id, BookDTO.BookDetailPatchRequest request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("Book not found", HttpStatus.NOT_FOUND));

        if (book.getBookDetail() != null) {
            applyDetailPatch(book.getBookDetail(), request);
        }
        return BookDTO.Response.fromEntity(book);
    }

    //상세 정보 null 체크 공통 메서드
    private void applyDetailPatch(BookDetail detail, BookDTO.BookDetailPatchRequest req) {
        if (req.getDescription() != null) detail.setDescription(req.getDescription());
        if (req.getLanguage() != null) detail.setLanguage(req.getLanguage());
        if (req.getPageCount() != null) detail.setPageCount(req.getPageCount());
        if (req.getPublisher() != null) detail.setPublisher(req.getPublisher());
        if (req.getCoverImageUrl() != null) detail.setCoverImageUrl(req.getCoverImageUrl());
        if (req.getEdition() != null) detail.setEdition(req.getEdition());
    }

    //도서 삭제
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("삭제할 도서가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}
