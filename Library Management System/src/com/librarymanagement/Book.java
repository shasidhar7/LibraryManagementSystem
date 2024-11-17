package com.librarymanagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private int quantity;

    // Constructor
    public Book(int id, String title, String author, String isbn, String genre, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.quantity = quantity;
    }

    public Book(String title, String author, String isbn, String genre, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
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

    public String getIsbn() {
        return isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Display Book Details
    public void displayBookDetails() {
        System.out.println("ID: " + id + ", Title: " + title + ", Author: " + author +
                ", ISBN: " + isbn + ", Genre: " + genre + ", Quantity: " + quantity);
    }

    // Add a Book to the Database
    public static void addBook(Book book) {
        String query = "INSERT INTO books (title, author, isbn, genre, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getQuantity());
            statement.executeUpdate();
            System.out.println("Book added: " + book.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update a Book in the Database
    public static void updateBook(int id, String title, String author, String isbn, String genre, int quantity) {
        String query = "UPDATE books SET title = ?, author = ?, isbn = ?, genre = ?, quantity = ? WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, isbn);
            statement.setString(4, genre);
            statement.setInt(5, quantity);
            statement.setInt(6, id);
            statement.executeUpdate();
            System.out.println("Book updated: " + title);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove a Book from the Database
    public static void removeBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Book removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get a Book by ID from the Database
    public static Book getBookById(int id) {
        String query = "SELECT * FROM books WHERE id = ?";
        Book book = null;
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getInt("quantity")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    // Get All Books from the Database
    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Connection connection = DBHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getInt("quantity")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Decrease Book Quantity (when borrowed)
    public static void decreaseQuantity(int bookId) {
        String query = "UPDATE books SET quantity = quantity - 1 WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
            System.out.println("Quantity decreased for book ID: " + bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Increase Book Quantity (when returned)
    public static void increaseQuantity(int bookId) {
        String query = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
            System.out.println("Quantity increased for book ID: " + bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
