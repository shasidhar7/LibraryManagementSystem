# Library Management System

## Overview

The **Library Management System** is a Java-based application designed to manage books and borrowers in a library. It provides functionality for adding, listing, borrowing, and returning books, as well as managing borrower information.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Clone the Repository](*clone-the-repository*)
- [Usage](#usage)

## Features

**Book Management:**

- Add new books to the library.
- Remove books from the library.
- Search books by title.
- List all books in the library.
- Update the quantity of books available.
  
**Borrower Management:**

- Add new borrowers to the library.
- Remove borrowers from the system.
- List all borrowers.
- Manage borrowing and returning of books.
  
**Book Borrowing & Returning:**

- Borrow books by reducing the available quantity.
- Return books and increase the available quantity.
- Track borrowed books in the borrowed_books table.
  
**Database Management:**

- Uses MySQL to manage and store information about books, borrowers, and borrowed book records.

## Technologies Used

- **Java:** Core programming language used for developing the application.
- **MySQL:** Relational database used for data storage and management.
- **JDBC:** Java Database Connectivity used for connecting to the MySQL database.

## Setup

1. **Clone the Repository**
   - Use the following command to clone the repository to your local machine:
     ```bash
     git clone https://github.com/shasidhar7/LibraryManagementSystem.git
     ```
2. **Configure Database**
   - Create a MySQL database named `librarymanagement`.
   - Run the provided SQL script to set up the tables:
     
     ```sql
     CREATE DATABASE librarymanagement;
     USE librarymanagement;

     CREATE TABLE books (
         id INT AUTO_INCREMENT PRIMARY KEY,
         title VARCHAR(255) NOT NULL,
         author VARCHAR(255) NOT NULL,
         isbn VARCHAR(20),
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
     ```

3. **Set Up Database Connection**
   - Update the `DBHelper` class with your MySQL credentials:
     ```java
     public class DBHelper {
         private static final String URL = "jdbc:mysql://localhost:3306/librarymanagement";
         private static final String USER = "your-username";
         private static final String PASSWORD = "your-password";

         // Add methods to establish the database connection
     }
     ```

4. **Build and Run**
   - Make sure your MySQL server is running.
   - Use your IDE to build and run the project.
     
## Usage
1. **Listing Books:**

    - Displays all books in the library or a message if no books are available.
2. **Adding a Book:**

    - Prompt for book details and add it to the database.
3. **Removing a Book:**

    - Provide the book ID to remove the book from the database. If the book is associated with any borrowed records, it will first delete those references.
4. **Searching Books:**

    - Search for books by entering a title or part of a title. It will display matching results or indicate if no matches are found.
5. **Managing Borrowers:**

    - Add new borrowers or remove existing ones from the system.
6. **Borrowing and Returning Books:**

    - Borrow a book by specifying the borrower ID, book ID, and due date.
    - Return a book to update the database and increase the book's quantity.
