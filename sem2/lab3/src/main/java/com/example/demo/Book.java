package com.example.demo;

public class Book {

    private static Long nextId = 1L;

    private Long id;
    private String title;
    private String author;
    private boolean available;
    private boolean read;

    // Конструктор по умолчанию
    public Book() {
        this.id = generateId();
        this.available = true;
    }

    // Конструктор с параметрами
    public Book(String title, String author) {
        this.id = generateId();
        this.title = title;
        this.author = author;
        this.available = true;
        this.read = false;
    }

    // Генерация уникального ID
    private synchronized Long generateId() {
        return nextId++;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    // 1 задание гет сет
    public boolean isRead() {
    return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "Книга{" +
                "id=" + id +
                ", название='" + title + '\'' +
                ", автор='" + author + '\'' +
                ", доступность=" + available +
                '}';
    }
}