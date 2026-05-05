package com.example.lab_3;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BookRepository repository;
    private ArrayList<Book> books;
    private ArrayAdapter<String> adapter;
    private ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioButton rb1 = findViewById(R.id.RadioButtonAuthor);
        RadioButton rb2 = findViewById(R.id.RadioButtonTitle);
        RadioButton rb3 = findViewById(R.id.RadioButtonGenre);

        rb1.setOnClickListener(v -> loadBooks(repository.sortBy("author")));

        rb2.setOnClickListener(v -> loadBooks(repository.sortBy("title")));

        rb3.setOnClickListener(v -> loadBooks(repository.sortBy("genre")));

        rb1.setVisibility(View.GONE);
        rb2.setVisibility(View.GONE);
        rb3.setVisibility(View.GONE);

        repository = new BookRepository(this);
        booksListView = findViewById(R.id.booksListView);
        Button addBookButton = findViewById(R.id.addBookButton);

        Button sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(v -> {
            rb1.setVisibility(View.VISIBLE);
            rb2.setVisibility(View.VISIBLE);
            rb3.setVisibility(View.VISIBLE);
        });

        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookActivity.class);
            startActivity(intent);
        });

        booksListView.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = books.get(position);

            Intent intent = new Intent(MainActivity.this, BookActivity.class);
            intent.putExtra("bookId", selectedBook.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RadioButton rb1 = findViewById(R.id.RadioButtonAuthor);
        RadioButton rb2 = findViewById(R.id.RadioButtonTitle);
        RadioButton rb3 = findViewById(R.id.RadioButtonGenre);

        rb1.setVisibility(View.GONE);
        rb2.setVisibility(View.GONE);
        rb3.setVisibility(View.GONE);

        loadBooks(repository.getAllBooks());
    }

    private void loadBooks(ArrayList<Book> loadedBooks) {
        try {
            books = loadedBooks;

            ArrayList<String> bookTitles = new ArrayList<>();

            for (Book book : books) {
                String createdAt = book.getCreatedAt().substring(0, 16);

                bookTitles.add(book.getTitle() + "\n" +
                        book.getAuthor() + " | " + book.getGenre() +
                        "\nCreated: " + createdAt);
            }

            adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    bookTitles
            );

            booksListView.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, "There is problem in loading books", Toast.LENGTH_SHORT).show();
        }
    }
}