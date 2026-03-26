package com.example.booktracker.service;

import com.example.booktracker.model.Book;
import com.example.booktracker.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Book business logic.
 * Handles CRUD operations for books.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Constructor injection of BookRepository.
     *
     * @param bookRepository the repository to inject
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieve all books.
     *
     * @return list of all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieve a book by its ID.
     *
     * @param id the book ID
     * @return Optional containing the book if found
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Save a new book.
     *
     * @param book the book to save
     * @return the saved book
     */
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Update an existing book.
     *
     * @param id          the ID of the book to update
     * @param bookDetails the updated book details
     * @return the updated book
     * @throws RuntimeException if book not found
     */
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setStatus(bookDetails.getStatus());
        book.setRating(bookDetails.getRating());

        return bookRepository.save(book);
    }

    /**
     * Delete a book by its ID.
     *
     * @param id the ID of the book to delete
     * @throws RuntimeException if book not found
     */
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}