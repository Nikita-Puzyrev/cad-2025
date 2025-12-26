package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // =======================
    // Веб-методы
    // =======================

    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('BIBLIOTECAR')")
    public String index(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "index";
    }

    @PostMapping("/addBook")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('BIBLIOTECAR')")
    public String addBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('BIBLIOTECAR')")
    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable Long id, @RequestParam(required = false) Boolean available) {
        Book book = bookRepository.findById(id).orElseThrow();
        if (available != null) {
            book.setAvailable(available);
        } else {
            book.setAvailable(!book.isAvailable());
        }
        bookRepository.save(book);
        return "redirect:/";
    }

    @PostMapping("/readBook/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('BIBLIOTECAR')")
    public String readBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setRead(true);
        bookRepository.save(book);
        return "redirect:/";
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('BIBLIOTECAR')")
    public String availableBooks(Model model) {
        List<Book> books = bookRepository.findByAvailableTrue();
        model.addAttribute("books", books);
        return "index";
    }

    // =======================
    // REST-методы
    // =======================

    @GetMapping("/api")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('BIBLIOTECAR')")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/api/available")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('BIBLIOTECAR')")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> books = bookRepository.findByAvailableTrue();
        return ResponseEntity.ok(books);
    }

    @PostMapping("/api/updateBook/{id}")
    @PreAuthorize("hasRole('BIBLIOTECAR')")
    public ResponseEntity<Book> apiUpdateBook(@PathVariable Long id, @RequestParam Boolean available) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setAvailable(available);
        bookRepository.save(book);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/api/readBook/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('BIBLIOTECAR')")
    public ResponseEntity<Book> apiReadBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setRead(true);
        bookRepository.save(book);
        return ResponseEntity.ok(book);
    }
}