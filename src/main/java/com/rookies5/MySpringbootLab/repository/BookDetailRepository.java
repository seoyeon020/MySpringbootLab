package com.rookies5.MySpringbootLab.repository;

import com.rookies5.MySpringbootLab.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookDetailRepository extends JpaRepository<BookDetail,Long> {
    Optional<BookDetail> findByBookId(Long bookId);

}
