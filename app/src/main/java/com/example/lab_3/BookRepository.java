package com.example.lab_3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class BookRepository {
    private final DatabaseHelper dbHelper;

    public BookRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM books ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            Book book = new Book(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("genre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("author")),
                    cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
            );
            books.add(book);
        }

        cursor.close();
        db.close();

        return books;
    }

    public Book getBookById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM books WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        Book book = null;

        if (cursor.moveToFirst()) {
            book = new Book(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("genre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("author")),
                    cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
            );
        }

        cursor.close();
        db.close();

        return book;
    }

    public void insertBook(Book book) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("genre", book.getGenre());
        values.put("author", book.getAuthor());

        db.insert("books", null, values);
        db.close();
    }

    public void updateBook(Book book) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("genre", book.getGenre());
        values.put("author", book.getAuthor());

        db.update(
                "books",
                values,
                "id = ?",
                new String[]{String.valueOf(book.getId())}
        );

        db.close();
    }

    public void deleteBook(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(
                "books",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }

    public boolean checkSameTitleAndAuthorExists(String title, String author, int currentBookId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM books WHERE title = ? AND author = ? AND id != ?",
                new String[]{title, author, String.valueOf(currentBookId)}
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

    public ArrayList<Book> sortBy(String sortOption) {
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        switch (sortOption) {
            case "author":
                cursor = db.rawQuery("SELECT * FROM books ORDER BY author DESC", null);
                break;

            case "title":
                cursor = db.rawQuery("SELECT * FROM books ORDER BY title DESC", null);
                break;

            case "genre":
                cursor = db.rawQuery("SELECT * FROM books ORDER BY genre DESC", null);
                break;
            default:
                cursor = db.rawQuery("SELECT * FROM books ORDER BY id DESC", null);
                break;
        }

        while (cursor.moveToNext()) {
            Book book = new Book(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("genre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("author")),
                    cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
            );
            books.add(book);
        }

        cursor.close();
        db.close();

        return books;
    }
}