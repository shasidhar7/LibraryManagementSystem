package com.librarymanagement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBook {
    private int id; // Borrowed book ID in the database
    private Book book; // The book being borrowed
    private LocalDate dueDate; // The due date for returning the book
    private int borrowerId; // The ID of the borrower

    // Constructor
    public BorrowedBook(int id, Book book, LocalDate dueDate, int borrowerId) {
        this.id = id;
        this.book = book;
        this.dueDate = dueDate;
        this.borrowerId = borrowerId;
    }

    public BorrowedBook(Book book, LocalDate dueDate, int borrowerId) {
        this.book = book;
        this.dueDate = dueDate;
        this.borrowerId = borrowerId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    // Display Borrowed Book Details
    public void displayDetails() {
        System.out.println("Book: " + book.getTitle() + ", Due Date: " + dueDate);
    }

    // Add Borrowed Book to Database
    public static void borrowBook(BorrowedBook borrowedBook) {
        String query = "INSERT INTO borrowed_books (book_id, borrower_id, due_date) VALUES (?, ?, ?)";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowedBook.getBook().getId());
            statement.setInt(2, borrowedBook.getBorrowerId());
            statement.setDate(3, Date.valueOf(borrowedBook.getDueDate()));
            statement.executeUpdate();
            System.out.println("\nBook borrowed: " + borrowedBook.getBook().getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Return a Borrowed Book (update the due date or remove it)
    public static boolean returnBook(int borrowedBookId) {
        String query = "DELETE FROM borrowed_books WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowedBookId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nBook returned.");
                return true; // Return true if the delete operation was successful
            } else {
                System.out.println("\nNo record found for the given borrowed book ID.");
                return false; // Return false if no rows were affected
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurred
        }
    }


    // Get Borrowed Book by ID
    public static BorrowedBook getBorrowedBookById(int id) {
        String query = "SELECT bb.id, bb.due_date, bb.borrower_id, b.title, b.author, b.isbn, b.genre, b.quantity " +
                       "FROM borrowed_books bb JOIN books b ON bb.book_id = b.id WHERE bb.id = ?";
        BorrowedBook borrowedBook = null;
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getInt("quantity")
                );
                borrowedBook = new BorrowedBook(
                        resultSet.getInt("id"),
                        book,
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getInt("borrower_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBook;
    }

    // Get All Borrowed Books by Borrower ID
    public static List<BorrowedBook> getAllBorrowedBooksByBorrower(int borrowerId) {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT bb.id, bb.due_date, b.title, b.author, b.isbn, b.genre, b.quantity " +
                       "FROM borrowed_books bb JOIN books b ON bb.book_id = b.id WHERE bb.borrower_id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getInt("quantity")
                );
                BorrowedBook borrowedBook = new BorrowedBook(
                        resultSet.getInt("id"),
                        book,
                        resultSet.getDate("due_date").toLocalDate(),
                        borrowerId
                );
                borrowedBooks.add(borrowedBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }
}
