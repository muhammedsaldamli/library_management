package com.muhammed.library_management.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.muhammed.library_management.entity.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
}