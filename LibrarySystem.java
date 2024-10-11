import java.util.*;

// Main Library System Class
public class LibrarySystem {
    private static Scanner sc = new Scanner(System.in);
    private static Library library = new Library();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Digital Library Management System ===");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. View All Books");
            System.out.println("5. View Issued Books");
            System.out.println("6. Generate Fine");
            System.out.println("7. Advance Booking");
            System.out.println("8. View Report");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    issueBook();
                    break;
                case 3:
                    returnBook();
                    break;
                case 4:
                    library.displayBooks();
                    break;
                case 5:
                    library.viewIssuedBooks();
                    break;
                case 6:
                    generateFine();
                    break;
                case 7:
                    advanceBooking();
                    break;
                case 8:
                    generateReport();
                    break;
                case 9:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addBook() {
        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Book Author: ");
        String author = sc.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = sc.nextLine();

        Book newBook = new Book(title, author, bookId);
        library.addBook(newBook);
        System.out.println("Book added successfully!");
    }

    private static void issueBook() {
        System.out.print("Enter Book ID to issue: ");
        String bookId = sc.nextLine();
        System.out.print("Enter User Name: ");
        String userName = sc.nextLine();

        library.issueBook(bookId, userName);
    }

    private static void returnBook() {
        System.out.print("Enter Book ID to return: ");
        String bookId = sc.nextLine();

        library.returnBook(bookId);
    }

    private static void generateFine() {
        System.out.print("Enter number of overdue days: ");
        int daysOverdue = sc.nextInt();
        sc.nextLine(); // consume newline

        double fine = library.calculateFine(daysOverdue);
        System.out.println("The fine is: $" + fine);
    }

    private static void advanceBooking() {
        System.out.print("Enter Book ID to reserve: ");
        String bookId = sc.nextLine();
        System.out.print("Enter User Name: ");
        String userName = sc.nextLine();

        library.reserveBook(bookId, userName);
    }

    private static void generateReport() {
        System.out.println("\nLibrary Report:");
        System.out.println("Total Books: " + library.getTotalBooks());
        System.out.println("Issued Books: " + library.getIssuedBooksCount());
        System.out.println("Reserved Books: " + library.getReservedBooksCount());
    }
}

// Book Class
class Book {
    private String title;
    private String author;
    private String bookId;
    private boolean isIssued;
    private String issuedTo;
    private String reservedBy;

    public Book(String title, String author, String bookId) {
        this.title = title;
        this.author = author;
        this.bookId = bookId;
        this.isIssued = false;
        this.issuedTo = null;
        this.reservedBy = null;
    }

    public String getBookId() {
        return bookId;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public void issueBook(String userName) {
        if (reservedBy == null || reservedBy.equals(userName)) {
            this.isIssued = true;
            this.issuedTo = userName;
            this.reservedBy = null; // Clear reservation after issuing
        } else {
            System.out.println("Book is reserved by another user.");
        }
    }

    public void returnBook() {
        this.isIssued = false;
        this.issuedTo = null;
    }

    public void reserveBook(String userName) {
        if (!isIssued && reservedBy == null) {
            this.reservedBy = userName;
            System.out.println("Book reserved successfully.");
        } else if (isIssued) {
            System.out.println("Book is currently issued, but reservation is placed.");
            this.reservedBy = userName;
        } else {
            System.out.println("Book is already reserved.");
        }
    }

    public boolean isReserved() {
        return reservedBy != null;
    }

    public String toString() {
        String status = isIssued ? "Issued to: " + issuedTo : (isReserved() ? "Reserved by: " + reservedBy : "Available");
        return "Book ID: " + bookId + ", Title: " + title + ", Author: " + author + ", Status: " + status;
    }
}

// Library Class
class Library {
    private List<Book> books;
    private final double FINE_PER_DAY = 1.5;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void displayBooks() {
        System.out.println("\nAll Books in the Library:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void viewIssuedBooks() {
        System.out.println("\nIssued Books:");
        for (Book book : books) {
            if (book.isIssued()) {
                System.out.println(book);
            }
        }
    }

    public void issueBook(String bookId, String userName) {
        for (Book book : books) {
            if (book.getBookId().equals(bookId) && !book.isIssued()) {
                book.issueBook(userName);
                System.out.println("Book issued to " + userName + " successfully.");
                return;
            }
        }
        System.out.println("Book not found or already issued.");
    }

    public void returnBook(String bookId) {
        for (Book book : books) {
            if (book.getBookId().equals(bookId) && book.isIssued()) {
                book.returnBook();
                System.out.println("Book returned successfully.");
                return;
            }
        }
        System.out.println("Book not found or not issued.");
    }

    public void reserveBook(String bookId, String userName) {
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                book.reserveBook(userName);
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public double calculateFine(int daysOverdue) {
        return daysOverdue * FINE_PER_DAY;
    }

    public int getTotalBooks() {
        return books.size();
    }

    public int getIssuedBooksCount() {
        int count = 0;
        for (Book book : books) {
            if (book.isIssued()) {
                count++;
            }
        }
        return count;
    }

    public int getReservedBooksCount() {
        int count = 0;
        for (Book book : books) {
            if (book.isReserved()) {
                count++;
            }
        }
        return count;
    }
}
