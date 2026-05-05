package com.example.lab_3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText genreEditText;
    private EditText authorEditText;
    private BookRepository repository;
    private int bookId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        repository = new BookRepository(this);

        titleEditText = findViewById(R.id.titleEditText);
        genreEditText = findViewById(R.id.genreEditText);
        authorEditText = findViewById(R.id.authorEditText);
        TextView editBookText = findViewById(R.id.editBookText);

        Button saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button backButton = findViewById(R.id.backButton);

        bookId = getIntent().getIntExtra("bookId", -1);

        if (bookId != -1) {
            loadBook();
        } else {
            deleteButton.setVisibility(View.GONE);
            editBookText.setVisibility(View.GONE);
        }

        saveButton.setOnClickListener(v -> saveBook());
        deleteButton.setOnClickListener(v -> deleteBook());
        backButton.setOnClickListener(v -> finish());
    }

    private void loadBook() {
        try {
            Book book = repository.getBookById(bookId);

            if (book != null) {
                titleEditText.setText(book.getTitle());
                genreEditText.setText(book.getGenre());
                authorEditText.setText(book.getAuthor());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error in showing book's properties", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBook() {
        try {
            String title = titleEditText.getText().toString().trim();
            String genre = genreEditText.getText().toString().trim();
            String author = authorEditText.getText().toString().trim();

            if (title.isEmpty() || genre.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Fill in the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (containsInvalidSymbols(author) || containsInvalidSymbols(genre)) {
                Toast.makeText(this, "Author and genre contain invalid symbols", Toast.LENGTH_SHORT).show();
                return;
            }

            if (repository.checkSameTitleAndAuthorExists(title, author, bookId)) {
                Toast.makeText(this, "Book with the same (title+author) exists", Toast.LENGTH_SHORT).show();
                return;
            }

            if (bookId == -1) {
                Book book = new Book(title, genre, author);
                repository.insertBook(book);
            } else {
                Book book = new Book(bookId, title, genre, author, null);
                repository.updateBook(book);
            }

            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error while saving a book", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBook() {
        try {
            repository.deleteBook(bookId);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erroe in deleting a book", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean containsInvalidSymbols(String text) {
        return !text.matches("[a-zA-Zа-яА-ЯіІїЇєЄґҐ\\s-]+");
    }
}