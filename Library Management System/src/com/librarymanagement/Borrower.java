package com.librarymanagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Borrower {
    private int id;
    private String name;
    private String contactDetails;

    // Constructor
    public Borrower(int id, String name, String contactDetails) {
        this.id = id;
        this.name = name;
        this.contactDetails = contactDetails;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    // Add Borrower to Database
    public static void addBorrower(Borrower borrower) {
        String query = "INSERT INTO borrowers (name, contact_details) VALUES (?, ?)";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, borrower.getName());
            statement.setString(2, borrower.getContactDetails());
            statement.executeUpdate();
            System.out.println("Borrower added: " + borrower.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Borrower in Database
    public static void updateBorrower(int id, String newContactDetails) {
        String query = "UPDATE borrowers SET contact_details = ? WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newContactDetails);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("Borrower updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove Borrower from Database
    public static void removeBorrower(int id) {
        String query = "DELETE FROM borrowers WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Borrower removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get All Borrowers from Database
    public static List<Borrower> getAllBorrowers() {
        List<Borrower> borrowers = new ArrayList<>();
        String query = "SELECT * FROM borrowers";
        try (Connection connection = DBHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Borrower borrower = new Borrower(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_details")
                );
                borrowers.add(borrower);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowers;
    }

    // Display Borrower Details
    public void displayBorrowerDetails() {
        System.out.println("ID: " + id + ", Name: " + name + ", Contact: " + contactDetails);
    }

    // Method to display borrowed books (retrieved from the database)
    public void displayBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = getBorrowedBooksFromDatabase();
        if (borrowedBooks.isEmpty()) {
            System.out.println(name + " has no borrowed books.");
        } else {
            System.out.println(name + " has borrowed the following books:");
            for (BorrowedBook book : borrowedBooks) {
                book.displayDetails();
            }
        }
    }

    // Get Borrowed Books from Database
    private List<BorrowedBook> getBorrowedBooksFromDatabase() {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT * FROM borrowed_books WHERE borrower_id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                Date dueDate = resultSet.getDate("due_date");

                // Assuming a BorrowedBook object takes a Book object and dueDate
                Book book = Book.getBookById(bookId);

                BorrowedBook borrowedBook = new BorrowedBook(book, dueDate.toLocalDate(), this.id);
                borrowedBooks.add(borrowedBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }

    // Borrow a Book (Add to borrowed_books Table)
    public void borrowBook(BorrowedBook book) {
        String query = "INSERT INTO borrowed_books (borrower_id, book_id, due_date) VALUES (?, ?, ?)";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.id);
            statement.setInt(2, book.getId());
            statement.setDate(3, Date.valueOf(book.getDueDate()));
            statement.executeUpdate();
            System.out.println(name + " borrowed the book: " + book.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Return a Book (Remove from borrowed_books Table)
    public boolean returnBook(int bookId) {
        String query = "DELETE FROM borrowed_books WHERE borrower_id = ? AND book_id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.id);
            statement.setInt(2, bookId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(name + " returned the book: " + bookId);
                return true;
            } else {
                System.out.println("Book not found in borrowed books.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
