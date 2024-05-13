package library;

import java.util.ArrayList;

/**
 *
 * @author NEIL
 */
public class Book {
      private String tittle, author, category;
      private String borrowDate, borrowReturn, name;

      private String borrowerName;
      private String returnDate;
       private boolean borrowed;
       private boolean available;
       private String title;
    private ArrayList<Book> borrowedBooks;
     
    public String getBorrowerName() {
        return borrowerName;
    }

    public String getReturnDate() {
        return returnDate;
    }
      
   

    
public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
        this.borrowedBooks = new ArrayList<>();
    }
 public Book(String title, String author, String category, boolean available) {
        this.tittle = title;
        this.author = author;
        this.category = category;
        this.available = available;
    }

    public Book(String name, String tittle, String author, String borrowDate, String returnDate) {
        this.name = name;
        this.tittle = tittle;
        this.author = author;
        this.borrowDate = borrowDate;
        this.borrowReturn = borrowReturn;
        this.returnDate = returnDate;
    }

  public void borrowBook(String borrowerName, String borrowDate, String returnDate) {
        this.available = false;
        borrowedBooks.add(new Book(title, author, borrowerName, borrowDate, returnDate));
    }
   public void returnBook(Book book) {
        this.available = true;
        borrowedBooks.remove(book);
    }

 //BORROW 
    public String getName() {
        return name;
    }
    public String getBorrowDate() {
        return borrowDate;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getBorrowReturn() {
        return borrowReturn;
    }

    public void setBorrowReturn(String borrowReturn) {
        this.borrowReturn = borrowReturn;
    }

    //SEARCH
    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
   public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }
    
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
}