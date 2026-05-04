package com.example.lab_3;



import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

        repository = new BookRepository(this);
        booksListView = findViewById(R.id.booksListView);
        Button addBookButton = findViewById(R.id.addBookButton);

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
        loadBooks();
    }

    private void loadBooks() {
        try {
            books = repository.getAllBooks();

            ArrayList<String> bookTitles = new ArrayList<>();


            for (Book book : books) {
                String createdAt = book.getCreatedAt().substring(0, 16);
                bookTitles.add(    book.getTitle() + "\n" +book.getAuthor()
                        + " | " + book.getGenre()
                        + "\n" + "Created: " + createdAt);
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