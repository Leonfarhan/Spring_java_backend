package com.farhanalwahid.backend.controller;

import com.farhanalwahid.backend.exception.ResourceNotFoundException;
import com.farhanalwahid.backend.model.Book;
import com.farhanalwahid.backend.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173/")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("/books")
    public Book newBook(@RequestBody Book newBook) {
        return bookRepository.save(newBook);
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@RequestBody Book newBookData, @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBookData.getTitle());
                    book.setAuthor(newBookData.getAuthor());
                    book.setPublisher(newBookData.getPublisher());
                    book.setPublicationYear(newBookData.getPublicationYear());
                    return bookRepository.save(book);
                }).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", "id", id);
        }
        bookRepository.deleteById(id);
        return "Book with id " + id + " has been deleted successfully.";
    }
}