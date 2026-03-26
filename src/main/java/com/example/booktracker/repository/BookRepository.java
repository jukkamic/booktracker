package com.example.booktracker.repository;

import com.example.booktracker.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Book entity.
 * Provides CRUD operations through Spring Data JPA.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}