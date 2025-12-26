package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        bookRepository.addBook(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public void deleteBook(Long id) {
        bookRepository.deleteBook(id);
    }

    public void updateAvailability(Long id, boolean available) {
        bookRepository.updateAvailability(id, available);
    }
    // 1 задание
    public void markAsRead(Long id) {
    bookRepository.markAsRead(id);
    }

    public List<Book> getAvailableBooks() {
    return bookRepository.getAvailableBooks();
    }
}