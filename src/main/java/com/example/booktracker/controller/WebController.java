package com.example.booktracker.controller;

import com.example.booktracker.model.Book;
import com.example.booktracker.model.BookStatus;
import com.example.booktracker.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web Controller for serving HTML pages.
 * Provides endpoints for the book tracker web interface.
 */
@Controller
public class WebController {

    private final BookService bookService;

    public WebController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Home page - displays all books.
     *
     * @param model the model to pass data to the view
     * @return the home view name
     */
    @GetMapping("/")
    public String home(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "index";
    }

    /**
     * Show form to add a new book.
     *
     * @param model the model to pass data to the view
     * @return the add book form view name
     */
    @GetMapping("/books/new")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("statuses", BookStatus.values());
        return "add-book";
    }

    /**
     * Process the add book form.
     *
     * @param book the book to add
     * @return redirect to home page
     */
    @PostMapping("/books")
    public String addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/";
    }

    /**
     * Show form to edit an existing book.
     *
     * @param id    the book ID
     * @param model the model to pass data to the view
     * @return the edit book form view name
     */
    @GetMapping("/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        model.addAttribute("book", book);
        model.addAttribute("statuses", BookStatus.values());
        return "edit-book";
    }

    /**
     * Process the edit book form.
     *
     * @param id          the book ID
     * @param bookDetails the updated book details
     * @return redirect to home page
     */
    @PostMapping("/books/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book bookDetails) {
        bookService.updateBook(id, bookDetails);
        return "redirect:/";
    }

    /**
     * Delete a book.
     *
     * @param id the book ID
     * @return redirect to home page
     */
    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/";
    }
}