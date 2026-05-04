package com.example.lab_3;

public class Book {
    private int id;
    private String title;
    private String genre;
    private String author;
    private String createdAt;

    public Book(int id, String title, String genre, String author, String createdAt) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.createdAt = createdAt;
    }

    public Book(String title, String genre, String author) {
        this.title = title;
        this.genre = genre;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}