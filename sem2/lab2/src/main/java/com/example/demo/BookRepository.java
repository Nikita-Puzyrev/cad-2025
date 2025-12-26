package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

    private List<Book> bookList = new ArrayList<>();

    // Добавить книгу
    public void addBook(Book book) {
        bookList.add(book);
    }

    // Получить все книги
    public List<Book> getAllBooks() {
        return bookList;
    }

    // Удалить книгу по ID
    public void deleteBook(Long id) {
        bookList.removeIf(book -> book.getId().equals(id));
    }

    // Обновить статус книги
    public void updateAvailability(Long id, boolean available) {
        for (Book book : bookList) {
            if (book.getId().equals(id)) {
                book.setAvailable(available);
                break;
            }
        }
    }
    // 1 Задание отметить книгу как прочитанную
    public void markAsRead(Long id) {
    for (Book book : bookList) {
        if (book.getId().equals(id)) {
            book.setRead(true);
            break;
            }
        }
    }
    // все еще 1 фильтрация по доступности
    public List<Book> getAvailableBooks() {
    return bookList.stream()
                   .filter(Book::isAvailable)
                   .toList();
    }
}