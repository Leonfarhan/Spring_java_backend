package com.farhanalwahid.backend.repository;

import com.farhanalwahid.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}