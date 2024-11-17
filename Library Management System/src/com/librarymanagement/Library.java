package com.librarymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Library {
    
    // Book Management Methods
    public void addBook(Book book) {
        String query = "INSERT INTO books (title, author, isbn, genre, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getQuantity());
            statement.executeUpdate();
            System.out.println("\nBook added: " + book.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(int id, String title, String author, String genre, int quantity) {
        String query = "UPDATE books SET title = ?, author = ?, genre = ?, quantity = ? WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, genre);
            statement.setInt(4, quantity);
            statement.setInt(5, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("\nBook updated: " + title);
            } else {
                System.out.println("\nBook with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBook(int id) {
    	// First, remove all references to this book in the borrowed_books table
        String deleteBorrowedBooksQuery = "DELETE FROM borrowed_books WHERE book_id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteBorrowedBooksQuery)) {
            statement.setInt(1, id);  
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("\nDeleted all references to the book with ID: " + id);
            } else {
                System.out.println("\nNo references found for the book with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Now, delete the book itself
        String deleteBookQuery = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteBookQuery)) {
            statement.setInt(1, id);  
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Book with ID: " + id + " has been deleted.");
            } else {
                System.out.println("Book with ID: " + id + " not found in the database.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listBooks() {
        String query = "SELECT * FROM books";
        try (Connection connection = DBHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
        	
        	// Flag to check if any books are found
        	boolean booksFound = false; 
        	
            while (resultSet.next()) {
            	booksFound = true; //set flag to true
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getInt("quantity")
                );
                book.displayBookDetails();
            }
            
            if(!booksFound) {
            	System.out.println("\nNo books found");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Borrower Management Methods
    //add borrower
    public void addBorrower(Borrower borrower) {
        String query = "INSERT INTO borrowers (name, contact_details) VALUES (?, ?)";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, borrower.getName());
            statement.setString(2, borrower.getContactDetails());
            statement.executeUpdate();
            System.out.println("\nBorrower added: " + borrower.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update borrower
    public void updateBorrower(int id, String newContactDetails) {
        String query = "UPDATE borrowers SET contact_details = ? WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newContactDetails);
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("\nBorrower updated.");
            } else {
                System.out.println("\nBorrower with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //remove borrower
    public void removeBorrower(int id) {
    	 // First, remove all references to this borrower in the borrowed_books table
        String deleteBorrowedBooksQuery = "DELETE FROM borrowed_books WHERE borrower_id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteBorrowedBooksQuery)) {
            statement.setInt(1, id);  
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("\nDeleted all references to the borrower with ID: " + id);
            } else {
                System.out.println("\nNo references found for the borrower with ID: " + id);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Now, delete the borrower from the borrowers table
        String deleteBorrowerQuery = "DELETE FROM borrowers WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteBorrowerQuery)) {
            statement.setInt(1, id);  // Using 'id' here for borrower
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Borrower with ID: " + id + " has been deleted.");
            } else {
                System.out.println("Borrower with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //list of borrowers
    public void listBorrowers() {
        String query = "SELECT * FROM borrowers";
        try (Connection connection = DBHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
        	
        	// Flag to check if any borrowers are found
        	boolean borrowersFound = false;
        	
            while (resultSet.next()) {
            	borrowersFound = true; // set flag to true
            	
                Borrower borrower = new Borrower(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_details")
                );
                borrower.displayBorrowerDetails();
            }
            
            if(!borrowersFound) {
            	System.out.println("\nNo borrowers found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to borrow a book
    public void borrowBook(int borrowerId, int bookId, int daysToDue) {
        Borrower borrower = findBorrowerById(borrowerId);
        Book book = findBookById(bookId);

        if (borrower != null && book != null && book.getQuantity() > 0) {
            LocalDate dueDate = LocalDate.now().plusDays(daysToDue);
            BorrowedBook borrowedBook = new BorrowedBook(book, dueDate, borrowerId);
            BorrowedBook.borrowBook(borrowedBook); // Store in borrowed_books database

            // Decrease the quantity of the book
            book.setQuantity(book.getQuantity() - 1);

            // Update the book quantity in the database
            updateBookQuantity(book);  

            System.out.println("\n" +borrower.getName() + " borrowed " + book.getTitle() + " book. Due date: " + dueDate);
        } else {
            System.out.println("\nCannot borrow book. Either the book is unavailable or the borrower/book ID is incorrect.");
        }
    }
    

    // Method to return a book
    public void returnBook(int borrowerId, int bookId) {
        Borrower borrower = findBorrowerById(borrowerId);
        Book book = findBookById(bookId);

        if (borrower != null && book != null) {
            // Fetch the correct borrowedBookId from the database
            int borrowedBookId = getBorrowedBookId(borrowerId, bookId);

            if (borrowedBookId > 0) {
                // Use the borrowedBookId to remove the borrowed book record from the database
                boolean removed = BorrowedBook.returnBook(borrowedBookId);

                if (removed) {
                    // Increase the quantity of the book
                    book.setQuantity(book.getQuantity() + 1);

                    // Update the book quantity in the database
                    updateBookQuantity(book);

                    System.out.println(borrower.getName() + " returned " + book.getTitle() + " book.");
                } else {
                    System.out.println("\nFailed to return the book. It may not be recorded as borrowed.");
                }
            } else {
                System.out.println("\nNo matching borrowed book record found.");
            }
        } else {
            System.out.println("\nCannot return book. Invalid borrower/book ID.");
        }
    }
    
  //method to update quantity in db
    private void updateBookQuantity(Book book) {
        String query = "UPDATE books SET quantity = ? WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, book.getQuantity());
            statement.setInt(2, book.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book quantity updated in the database.");
            } else {
                System.out.println("Failed to update book quantity.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getBorrowedBookId(int borrowerId, int bookId) {
        String query = "SELECT id FROM borrowed_books WHERE borrower_id = ? AND book_id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowerId);
            statement.setInt(2, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id"); // Return the borrowedBookId
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no record is found
    }


    // Helper methods to find a borrower or book by ID
    private Borrower findBorrowerById(int borrowerId) {
        String query = "SELECT * FROM borrowers WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Borrower(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_details")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Book findBookById(int bookId) {
        String query = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Book(
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
        return null;
    }
    
    // Method to search for books by title
    public void searchBooksByTitle(String title) {
        String query = "SELECT * FROM books WHERE title LIKE ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + title + "%");
            ResultSet resultSet = statement.executeQuery();
            
            // Check if the result set is empty
            if (!resultSet.isBeforeFirst()) { // isBeforeFirst() returns false if the ResultSet is empty
                System.out.println("\nNo books found with the title: " + title);
                return;
            }
            
            // If there are results, display them
            System.out.println("\nfollowing book/s found as for your search:");
            while (resultSet.next()) {
                displayBookAvailability(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to search for books by author
    public void searchBooksByAuthor(String author) {
        String query = "SELECT * FROM books WHERE author LIKE ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + author + "%");
            ResultSet resultSet = statement.executeQuery();
            
            // Check if the result set is empty
            if (!resultSet.isBeforeFirst()) { // isBeforeFirst() returns false if the ResultSet is empty
                System.out.println("\nNo books found with the author: " + author);
                return;
            }
            
            System.out.println("\nfollowing book/s found as for your search:");
            while (resultSet.next()) {
                displayBookAvailability(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to search for books by genre
    public void searchBooksByGenre(String genre) {
        String query = "SELECT * FROM books WHERE genre LIKE ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + genre + "%");
            ResultSet resultSet = statement.executeQuery();
            
            // Check if the result set is empty
            if (!resultSet.isBeforeFirst()) { // isBeforeFirst() returns false if the ResultSet is empty
                System.out.println("\nNo books found with the genre: " + genre);
                return;
            }
            
            System.out.println("\nfollowing book/s found as for your search:");
            while (resultSet.next()) {
                displayBookAvailability(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display book availability
    private void displayBookAvailability(ResultSet resultSet) throws SQLException {
        System.out.println("Title: " + resultSet.getString("title") + ", Author: " + resultSet.getString("author") +
                           ", Genre: " + resultSet.getString("genre") + ", Available Copies: " + resultSet.getInt("quantity"));
    }
}
