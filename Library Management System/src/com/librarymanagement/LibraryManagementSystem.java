package com.librarymanagement;

import java.util.Scanner;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library(); // Assuming Library class is modified to handle DB operations
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nLibrary Management System:");
            System.out.println("--------------------------");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Remove Book");
            System.out.println("4. List All Books");
            System.out.println();
            System.out.println("5. Add Borrower");
            System.out.println("6. Update Borrower");
            System.out.println("7. Remove Borrower");
            System.out.println("8. List All Borrowers");
            System.out.println();
            System.out.println("9. Borrow Book");
            System.out.println("10. Return Book");
            System.out.println();
            System.out.println("11. Search Book by Title");
            System.out.println("12. Search Book by Author");
            System.out.println("13. Search Book by Genre");
            System.out.println("14. Exit");
            System.out.println("---------------------------");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // newline

            switch (choice) {
                case 1:
                    // Add Book
                    System.out.print("Enter book ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // newline
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    library.addBook(new Book(id, title, author, isbn, genre, quantity));
                    break;

                case 2:
                    // Update Book
                    System.out.print("Enter book ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine(); // newline
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new author: ");
                    String newAuthor = scanner.nextLine();
                    System.out.print("Enter new genre: ");
                    String newGenre = scanner.nextLine();
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    library.updateBook(updateId, newTitle, newAuthor, newGenre, newQuantity);
                    break;

                case 3:
                    // Remove Book
                    System.out.print("Enter book ID to remove: ");
                    int removeId = scanner.nextInt();
                    library.removeBook(removeId);
                    break;

                case 4:
                    // List All Books
                    library.listBooks();
                    break;

                case 5:
                    // Add Borrower
                    System.out.print("Enter borrower ID: ");
                    int borrowerId = scanner.nextInt();
                    scanner.nextLine(); // newline
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter contact details: ");
                    String contactDetails = scanner.nextLine();
                    library.addBorrower(new Borrower(borrowerId, name, contactDetails));
                    break;

                case 6:
                    // Update Borrower
                    System.out.print("Enter borrower ID to update: ");
                    int updateBorrowerId = scanner.nextInt();
                    scanner.nextLine(); // newline
                    System.out.print("Enter new contact details: ");
                    String newContactDetails = scanner.nextLine();
                    library.updateBorrower(updateBorrowerId, newContactDetails);
                    break;

                case 7:
                    // Remove Borrower
                    System.out.print("Enter borrower ID to remove: ");
                    int removeBorrowerId = scanner.nextInt();
                    library.removeBorrower(removeBorrowerId);
                    break;

                case 8:
                    // List All Borrowers
                    library.listBorrowers();
                    break;

                case 9:
                    // Borrow Book
                    System.out.print("Enter borrower ID: ");
                    borrowerId = scanner.nextInt();
                    System.out.print("Enter book ID: ");
                    int bookId = scanner.nextInt();
                    System.out.print("Enter number of days until due: ");
                    int daysToDue = scanner.nextInt();
                    library.borrowBook(borrowerId, bookId, daysToDue);
                    break;

                case 10:
                    // Return Book
                    System.out.print("Enter borrower ID: ");
                    borrowerId = scanner.nextInt();
                    System.out.print("Enter book ID: ");
                    bookId = scanner.nextInt();
                    library.returnBook(borrowerId, bookId);
                    break;

                case 11:
                    // Search Book by Title
                    System.out.print("Enter title to search: ");
                    title = scanner.nextLine();
                    library.searchBooksByTitle(title);
                    break;

                case 12:
                    // Search Book by Author
                    System.out.print("Enter author to search: ");
                    author = scanner.nextLine();
                    library.searchBooksByAuthor(author);
                    break;

                case 13:
                    // Search Book by Genre
                    System.out.print("Enter genre to search: ");
                    genre = scanner.nextLine();
                    library.searchBooksByGenre(genre);
                    break;

                case 14:
                    // Exit
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
