**Library Management System**
The Library Management System is a Java-based application designed to manage books and borrowers in a library. It provides functionality for adding, listing, borrowing, and returning books, as well as managing borrower information.

Table of Contents
Features
Technologies Used
Setup
Database Schema
Usage
License
Features
Book Management:

Add new books to the library.
Remove books from the library.
Search books by title.
List all books in the library.
Update the quantity of books available.
Borrower Management:

Add new borrowers to the library.
Remove borrowers from the system.
List all borrowers.
Manage borrowing and returning of books.
Book Borrowing & Returning:

Borrow books by reducing the available quantity.
Return books and increase the available quantity.
Track borrowed books in the borrowed_books table.
Database Management:

Uses MySQL to manage and store information about books, borrowers, and borrowed book records.
Technologies Used
Java: Core programming language used for developing the application.
MySQL: Relational database used for data storage and management.
JDBC: Java Database Connectivity used for connecting to the MySQL database.
Setup
Prerequisites:

Ensure that you have Java and MySQL installed on your system.
Configure your MySQL database and create the necessary tables as per the schema provided.
Database Configuration:

Update the DBHelper class with your MySQL connection details:
java
Copy code
// Example configuration in DBHelper class
private static final String URL = "jdbc:mysql://localhost:3306/librarymanagement";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
Create a database named librarymanagement in MySQL.
Creating Tables:

Use the following SQL script to create the required tables:
sql
Copy code
CREATE DATABASE librarymanagement;

USE librarymanagement;

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    isbn VARCHAR(50),
    genre VARCHAR(50),
    quantity INT NOT NULL
);

CREATE TABLE borrowers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_details VARCHAR(255)
);

CREATE TABLE borrowed_books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    borrower_id INT,
    book_id INT,
    due_date DATE,
    FOREIGN KEY (borrower_id) REFERENCES borrowers(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
Running the Application:

Compile and run the LibraryManagementSystem class to start the application.
Follow the prompts in the console to perform various library management operations.
Usage
Listing Books:

Displays all books in the library or a message if no books are available.
Adding a Book:

Prompt for book details and add it to the database.
Removing a Book:

Provide the book ID to remove the book from the database. If the book is associated with any borrowed records, it will first delete those references.
Searching Books:

Search for books by entering a title or part of a title. It will display matching results or indicate if no matches are found.
Managing Borrowers:

Add new borrowers or remove existing ones from the system.
Borrowing and Returning Books:

Borrow a book by specifying the borrower ID, book ID, and due date.
Return a book to update the database and increase the book's quantity.
