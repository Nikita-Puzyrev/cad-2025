package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Главная страница библиотеки
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "index";
    }

    // Добавление книги
    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book) {
        bookService.addBook(book);
        return "redirect:/";
    }

    // Удаление книги
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/";
    }

    // Обновление статуса книги + 1 задание теперь кнопочка жмется в обе стороны хихихихиххи
    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable Long id, @RequestParam boolean available) {
    Book book = bookService.getAllBooks().stream()
                           .filter(b -> b.getId().equals(id))
                           .findFirst()
                           .orElse(null);
        if (book != null) {
            bookService.updateAvailability(id, !book.isAvailable());}
        return "redirect:/";
    }

    // Отметить книгу как прочитанную
    @PostMapping("/readBook/{id}")
    public String readBook(@PathVariable Long id) {
        bookService.markAsRead(id);
        return "redirect:/";
    }

    // Отображение только доступных книг
    @GetMapping("/available")
    public String availableBooks(Model model) {
        model.addAttribute("books", bookService.getAvailableBooks());
        return "index";
    }
}