package library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Dashboard extends javax.swing.JFrame {

    private List<Object[]> rowData = new ArrayList<>();
    private ArrayList<Book> books;
    private ArrayList<Book> borrowedBooks = new ArrayList<>();
    private ArrayList<String[]> borrowRecords = new ArrayList<>();
    private List<Book> notAvailableBooks = new ArrayList<>();
    private ArrayList<BorrowedBook> borrowedBooks1 = new ArrayList<>();
    private ArrayList<BorrowedBook> hiram = new ArrayList<>();
    private ArrayList<Book> availableBooks = new ArrayList<>();
    private JTextField jtBorrowName;
    private int selectedRowIndex = -1;
    private StaffForm staffForm;
    private JTextField jtReturnTitle;

    public Dashboard(ArrayList<Book> books) {
        this.books = books;
        initComponents();
        refreshBooksTable();
        jTableAvailable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    selectedRowIndex = jTableAvailable.getSelectedRow();
                }
            }
        });
    }

    public Dashboard() {
        this.books = listOfBooks(); 
        initComponents();
        refreshBooksTable();
        jTableAvailable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    selectedRowIndex = jTableAvailable.getSelectedRow();
                }
            }
        });
    }

    public ArrayList<Book> listOfBooks() {
        ArrayList<Book> list = new ArrayList<>();
        list.add(new Book("Computer Programming 3", "Neil Sindayen", "Programming", true));
        list.add(new Book("Discrete Structure 2", "Neil Sindayen", "Programming", true));
        list.add(new Book("Discrete Structure 1", "Vladlen Koltun", "Programming", true));
        list.add(new Book("Electrical Circuits 1", "Neil Sindayen", "Engineering", true));
        list.add(new Book("Electrical Circuits 2", "Neil Sindayen", "Engineering", true));
        list.add(new Book("Environmental Engineering", "Neil Sindayen", "Engineering", true));
        list.add(new Book("Economics", "Neil Sindayen", "Engineering", true));
        list.add(new Book("Financial Management", "Neil Sindayen", "Business & Accounting", true));
        list.add(new Book("Investment & Portfolio Management", "Neil Sindayen", "Business & Accounting", true));
        list.add(new Book("Taxation", "Neil Sindayen", "Business & Accounting", true));
        return list;
    }

    public ArrayList<Book> getAllAvailableBooks() {
        ArrayList<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public ArrayList<Book> getAllNotAvailableBooks() {
        ArrayList<Book> notAvailableBooks = new ArrayList<>();
        for (Book book : books) {
            if (!book.isAvailable()) {
                notAvailableBooks.add(book);
            }
        }
        return notAvailableBooks;
    }

    private void addRowToTable() {
        DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
        model.setRowCount(0); // Clear the table

        for (Book book : getAllAvailableBooks()) {
            Object[] row = {book.getTittle(), book.getAuthor(), book.getCategory()};
            model.addRow(row);
        }
    }

    private boolean isBookBorrowed(Book book) {
        for (Book notAvailableBook : getAllNotAvailableBooks()) {
            if (notAvailableBook.getTittle().equals(book.getTittle()) && notAvailableBook.getAuthor().equals(book.getAuthor())) {
                System.out.println("Book is borrowed: " + notAvailableBook.getTittle() + " by " + notAvailableBook.getAuthor());
                return true;
            }
        }
        return false;
    }

    public ArrayList<Book> searchBooksByTitle(String title) {
        ArrayList<Book> filteredList = new ArrayList<>();
        for (Book book : books) {
            if (book.getTittle().toLowerCase().contains(title.toLowerCase())) {
                filteredList.add(book);
            }
        }
        return filteredList;
    }

    public ArrayList<Book> searchBooks(String searchTitle, String searchAuthor) {
        ArrayList<Book> filteredList = new ArrayList<>();
        for (Book book : books) {
            String title = book.getTittle().toLowerCase();
            String author = book.getAuthor().toLowerCase();
            if (title.contains(searchTitle.toLowerCase()) && author.contains(searchAuthor.toLowerCase())) {
                filteredList.add(book);
            }
        }
        return filteredList;
    }

    public void addRowToTable(ArrayList<Book> filteredList) {
        DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
        model.setRowCount(0); // Clear existing rows
        for (int i = 0; i < filteredList.size(); i++) {
            model.addRow(new Object[]{
                filteredList.get(i).getTittle(),
                filteredList.get(i).getAuthor()
            });

            // Highlight the row if it matches the search term (title or author)
            String searchTitle = jtSearchTittle.getText().toLowerCase();
            String searchAuthor = jtSearchAuthor.getText().toLowerCase();
            String currentTitle = filteredList.get(i).getTittle().toLowerCase();
            String currentAuthor = filteredList.get(i).getAuthor().toLowerCase();
            if (currentTitle.contains(searchTitle) || currentAuthor.contains(searchAuthor)) {
                jTableAvailable.setRowSelectionInterval(i, i);
            }
        }
    }

    public void borrowBook(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
        Book book = books.get(rowIndex);

        // Check if the book is available
        if (book.isAvailable()) {
            book.setAvailable(false); // Mark the book as not available

            // Remove the row from the table
            model.removeRow(rowIndex);

            // Add the borrowed book to the borrowedBooks list
            borrowedBooks.add(book);

            // Remove the borrowed book from the books list
            books.remove(book);

            // Add the borrowed book to the borrowedBooks1 list
            addBorrowedBook(book.getTittle(), book.getAuthor(), "borrowerName", "borrowDate", "returnDate");

            // Update the table in StaffForm
            if (staffForm != null) {
                staffForm.populateTable1();
                staffForm.populateTable2();
            }
        } else {
            // Book is already borrowed, show a message or handle it as needed
        }
    }

    public void updateAvailableBooksTable() {
        DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
        model.setRowCount(0); // Clear the table

        for (Book book : books) {
            if (book.isAvailable()) {
                model.addRow(new Object[]{book.getTittle(), book.getAuthor(), book.getCategory()});
            }
        }
    }

    public void refreshBooksTable() {
        updateAvailableBooksTable();  // Assuming this method correctly refreshes the table
    }

    private void checkBorrowedBooksButtonActionPerformed(java.awt.event.ActionEvent evt) {
        TheLastOne();
    }

    public ArrayList<BorrowedBook> TheLastOne() {
        ArrayList<BorrowedBook> theLastOne = new ArrayList<>();
        for (BorrowedBook borrowedBook : borrowedBooks1) {
            theLastOne.add(borrowedBook);
        }
        for (BorrowedBook borrowedBook : theLastOne) {
            System.out.println("Title: " + borrowedBook.getTitle());
            System.out.println("Author: " + borrowedBook.getAuthor());
            System.out.println("Borrower Name: " + borrowedBook.getBorrowerName());
            System.out.println("Borrow Date: " + borrowedBook.getBorrowDate());
            System.out.println("Return Date: " + borrowedBook.getReturnDate());
            System.out.println("--------------------------");
        }
        return theLastOne;
    }

    private void updateTableWithAvailableBooks() {
        DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
        model.setRowCount(0); // Clear previous table data

        for (Book book : listOfBooks()) {
            if (book.isAvailable()) {
                model.addRow(new Object[]{book.getTittle(), book.getAuthor(), book.getCategory()});
            }
        }
    }

    public void addBorrowedBook(String title, String author, String borrowerName, String borrowDate, String returnDate) {
        BorrowedBook borrowedBook = new BorrowedBook(title, author, borrowerName, borrowDate, returnDate);
        borrowedBooks1.add(borrowedBook);
    }





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jbLogin = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jpSearch = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtSearchTittle = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtSearchAuthor = new javax.swing.JTextField();
        jbSearch = new javax.swing.JButton();
        jpBorrow = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtBorrowerName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtBorrowDate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtReturnDate = new javax.swing.JTextField();
        jbBorrow = new javax.swing.JButton();
        jpBorrow1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jtReturnTittle = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jtReturnAuthor1 = new javax.swing.JTextField();
        jbReturnBook = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jtReturnDate1 = new javax.swing.JTextField();
        jtReturnName1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAvailable = new javax.swing.JTable();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableBorrowed = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jComboBoxIndex = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

        jPanel1.setBackground(java.awt.Color.lightGray);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("CCS Library");

        jbLogin.setBackground(new java.awt.Color(51, 51, 255));
        jbLogin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jbLogin.setForeground(new java.awt.Color(255, 255, 255));
        jbLogin.setText("LOGIN");
        jbLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLoginActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("REFRESH");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1013, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpSearch.setBackground(java.awt.Color.lightGray);

        jLabel2.setText("Search");

        jtSearchTittle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtSearchTittleActionPerformed(evt);
            }
        });

        jLabel3.setText("Title :");

        jLabel4.setText("Author:");

        jtSearchAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtSearchAuthorActionPerformed(evt);
            }
        });

        jbSearch.setText("SEARCH");
        jbSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpSearchLayout = new javax.swing.GroupLayout(jpSearch);
        jpSearch.setLayout(jpSearchLayout);
        jpSearchLayout.setHorizontalGroup(
            jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpSearchLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpSearchLayout.createSequentialGroup()
                            .addGroup(jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4))
                            .addGap(32, 32, 32)
                            .addGroup(jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jtSearchTittle, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jtSearchAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel2))
                    .addComponent(jbSearch))
                .addGap(67, 67, 67))
        );
        jpSearchLayout.setVerticalGroup(
            jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtSearchTittle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtSearchAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jbSearch)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jpBorrow.setBackground(java.awt.Color.lightGray);

        jLabel5.setText("Borrow");

        jtBorrowerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBorrowerNameActionPerformed(evt);
            }
        });

        jLabel6.setText("Name :");

        jLabel7.setText("Barrow date :");

        jtBorrowDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBorrowDateActionPerformed(evt);
            }
        });

        jLabel11.setText("Return date :");

        jtReturnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtReturnDateActionPerformed(evt);
            }
        });

        jbBorrow.setText("BORROW");
        jbBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBorrowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpBorrowLayout = new javax.swing.GroupLayout(jpBorrow);
        jpBorrow.setLayout(jpBorrowLayout);
        jpBorrowLayout.setHorizontalGroup(
            jpBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBorrowLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jpBorrowLayout.createSequentialGroup()
                        .addGroup(jpBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11))
                        .addGap(32, 32, 32)
                        .addGroup(jpBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtBorrowDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtBorrowerName, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBorrowLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbBorrow)
                .addGap(64, 64, 64))
        );
        jpBorrowLayout.setVerticalGroup(
            jpBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBorrowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtBorrowerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtBorrowDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jbBorrow)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jpBorrow1.setBackground(java.awt.Color.lightGray);

        jLabel8.setText("Return");

        jtReturnTittle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtReturnTittleActionPerformed(evt);
            }
        });

        jLabel9.setText("Name  :");

        jLabel10.setText("Title    :");

        jLabel12.setText("Author :");

        jtReturnAuthor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtReturnAuthor1ActionPerformed(evt);
            }
        });

        jbReturnBook.setText("RETURN");
        jbReturnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReturnBookActionPerformed(evt);
            }
        });

        jLabel16.setText("Return date :");

        jtReturnDate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtReturnDate1ActionPerformed(evt);
            }
        });

        jtReturnName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtReturnName1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpBorrow1Layout = new javax.swing.GroupLayout(jpBorrow1);
        jpBorrow1.setLayout(jpBorrow1Layout);
        jpBorrow1Layout.setHorizontalGroup(
            jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBorrow1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpBorrow1Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addGap(59, 59, 59)
                            .addComponent(jtReturnAuthor1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBorrow1Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbReturnBook)
                            .addGap(27, 27, 27)))
                    .addComponent(jLabel8)
                    .addGroup(jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jpBorrow1Layout.createSequentialGroup()
                            .addComponent(jLabel16)
                            .addGap(32, 32, 32)
                            .addComponent(jtReturnDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jpBorrow1Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGap(61, 61, 61)
                            .addComponent(jtReturnName1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jpBorrow1Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(65, 65, 65)
                            .addComponent(jtReturnTittle, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jpBorrow1Layout.setVerticalGroup(
            jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBorrow1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtReturnName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jtReturnTittle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jtReturnDate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jpBorrow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jtReturnAuthor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbReturnBook)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jTableAvailable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Author"
            }
        ));
        jTableAvailable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAvailableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableAvailable);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"All", "Business & Accounting", "Engineering", "Programming"}));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel13.setText("Category :");

        jTableBorrowed.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Author", "Name", "Barrow date", "Return date"
            }
        ));
        jTableBorrowed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableBorrowedMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableBorrowed);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("BOOKS");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Borrowed Books");

        jLabel17.setText("Index :");

        jComboBoxIndex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"All","A", "B", "C", "D", "E", "F", "G", "I", "J", "K", "L", "M", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));
        jComboBoxIndex.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jComboBoxIndex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIndexActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpBorrow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpBorrow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addGap(281, 281, 281))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17)
                    .addComponent(jComboBoxIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jpBorrow1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jtSearchTittleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtSearchTittleActionPerformed
       jbSearchActionPerformed(evt);
    }//GEN-LAST:event_jtSearchTittleActionPerformed

    private void jtSearchAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtSearchAuthorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtSearchAuthorActionPerformed

    private void jtBorrowerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBorrowerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBorrowerNameActionPerformed

    private void jtBorrowDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBorrowDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBorrowDateActionPerformed

    private void jtReturnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtReturnDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtReturnDateActionPerformed

    private void jbBorrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbBorrowActionPerformed
     String borrowerName = jtBorrowerName.getText().trim();
     String borrowDate = jtBorrowDate.getText().trim();
     String returnDate = jtReturnDate.getText().trim();
     int selectedRow = jTableAvailable.getSelectedRow();
    if (selectedRow != -1) {
        

        // Check if the fields are empty
        if (borrowerName.isEmpty() || borrowDate.isEmpty() || returnDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // Check if the date formats are correct
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        dateFormat.setLenient(false); // Ensure strict date parsing
        try {
            dateFormat.parse(borrowDate);
            dateFormat.parse(returnDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use MM/DD/YY.");
            return;
        }

        // Find the book in the books list and set its availability to false
        DefaultTableModel modelAvailable = (DefaultTableModel) jTableAvailable.getModel();
        String title = (String) modelAvailable.getValueAt(selectedRow, 0);
        String author = (String) modelAvailable.getValueAt(selectedRow, 1);
        for (Book book : books) {
            if (book.getTittle().equals(title) && book.getAuthor().equals(author)) {
                book.setAvailable(false);
                notAvailableBooks.add(book); // Add the book to the notAvailableBooks list
                BorrowedBook borrowedBook = new BorrowedBook(title, author, borrowerName, borrowDate, returnDate);
                borrowedBooks1.add(borrowedBook); // Add the borrowed book to the list
                break;
            }
            
            TheLastOne();
        }
        

        // Update the table to reflect the changes
        modelAvailable.removeRow(selectedRow);
        
        // Display the borrowed book on jTableBorrowed
        DefaultTableModel modelBorrowed = (DefaultTableModel) jTableBorrowed.getModel();
        modelBorrowed.addRow(new Object[]{title, author, borrowerName, borrowDate, returnDate});
        checkBorrowedBooksButtonActionPerformed(null);
    }
    }//GEN-LAST:event_jbBorrowActionPerformed

    private void jtReturnAuthor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtReturnAuthor1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtReturnAuthor1ActionPerformed

    private void jtReturnTittleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtReturnTittleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtReturnTittleActionPerformed

    private void jTableAvailableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAvailableMouseClicked
        // THIS IS THE CODE TO LOAD THE DATA TO THE JTEXTFIELDS
        DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
        int setSelectedRow = jTableAvailable.getSelectedRow();
   
    }//GEN-LAST:event_jTableAvailableMouseClicked
//to LoginForm
private void jbLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLoginActionPerformed
    ArrayList<Book> books = new ArrayList<>(listOfBooks()); // Initialize the shared books list
    Dashboard dashboard = new Dashboard(books);
    LoginForm loginForm = new LoginForm();
    loginForm.setDashboard(dashboard);
    loginForm.setBooksList(books); // Pass the shared books list to LoginForm
    loginForm.setVisible(true);
}//GEN-LAST:event_jbLoginActionPerformed




//For Category
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
  String selectedCategory = (String) jComboBox1.getSelectedItem();

    DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
    model.setRowCount(0); // Clear previous table data

    ArrayList<Book> availableBooks = getAllAvailableBooks();
    for (Book book : availableBooks) {
        if (selectedCategory.equals("All") || selectedCategory.equals(book.getCategory())) {
            model.addRow(new Object[]{book.getTittle(), book.getAuthor(), "Available"});
        }
    }
}

private boolean isBookBorrowed(String title, String author) {
    for (Book book : borrowedBooks) {
        if (book.getTittle().equals(title) && book.getAuthor().equals(author)) {
            return true;
        }
    }
    return false;
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jbSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSearchActionPerformed
    String searchTitle = jtSearchTittle.getText().toLowerCase();
    String searchAuthor = jtSearchAuthor.getText().toLowerCase();

    DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
    model.setRowCount(0); // Clear previous table data

    ArrayList<Book> allBooks = getAllAvailableBooks();
    for (Book book : allBooks) {
        if (book.getTittle().toLowerCase().contains(searchTitle) && book.getAuthor().toLowerCase().contains(searchAuthor) && !isBookBorrowed(book.getTittle(), book.getAuthor())) {
            model.addRow(new Object[]{book.getTittle(), book.getAuthor(), "Available"});
        }
    }
    }//GEN-LAST:event_jbSearchActionPerformed
public void setStaffForm(StaffForm staffForm) {
        this.staffForm = staffForm;
    }
    private void jtReturnDate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtReturnDate1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtReturnDate1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jbReturnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbReturnBookActionPerformed
 int selectedRow = jTableBorrowed.getSelectedRow();
    if (selectedRow != -1) {
        String title = (String) jTableBorrowed.getValueAt(selectedRow, 0);
        String author = (String) jTableBorrowed.getValueAt(selectedRow, 1);
        String borrowerName = (String) jTableBorrowed.getValueAt(selectedRow, 2);

        // Remove the returned book from the notAvailableBooks list
        for (Book book : notAvailableBooks) {
            if (book.getTittle().equals(title) && book.getAuthor().equals(author) && book.getName().equals(borrowerName)) {
                notAvailableBooks.remove(book);
                break;
            }
        }


        for (Book book : books) {
            if (book.getTittle().equals(title) && book.getAuthor().equals(author)) {
                book.setAvailable(true);
                availableBooks.add(book);
                break;
            }
        }

    
        addRowToTable();

        //populateBorrowedTable();

        // Show a message indicating the book has been returned
        JOptionPane.showMessageDialog(this, "Book returned successfully.");
    } else {
        JOptionPane.showMessageDialog(this, "Please select a book to return.");
    }
}

private void updateTableModel() {
    DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
    model.setRowCount(0); // Clear the table

    // Add only available books to the table
    for (Book book : getAllAvailableBooks()) {
        Object[] row = {book.getTittle(), book.getAuthor()};
        model.addRow(row);
    }
    }//GEN-LAST:event_jbReturnBookActionPerformed

    private void jtReturnName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtReturnName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtReturnName1ActionPerformed

    private void jComboBoxIndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIndexActionPerformed
     String selectedIndex = (String) jComboBoxIndex.getSelectedItem();
    filterTable(selectedIndex);
}

private void filterTable(String index) {
    DefaultTableModel model = (DefaultTableModel) jTableAvailable.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTableAvailable.setRowSorter(sorter);

    if ("All".equals(index)) {
        sorter.setRowFilter(null);
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("^" + index, 0)); // Assuming the first column is the title column
    }
    }//GEN-LAST:event_jComboBoxIndexActionPerformed

    private void jTableBorrowedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBorrowedMouseClicked
        int selectedRow = jTableBorrowed.getSelectedRow();
        if (selectedRow != -1) {
            // Get the data from the selected row
            String title = (String) jTableBorrowed.getValueAt(selectedRow, 0);
            String author = (String) jTableBorrowed.getValueAt(selectedRow, 1);
            String name = (String) jTableBorrowed.getValueAt(selectedRow, 2);
            String borrowDate = (String) jTableBorrowed.getValueAt(selectedRow, 3);
            String returnDate = (String) jTableBorrowed.getValueAt(selectedRow, 4);

            // Do something with the data, such as displaying it in a dialog
            JOptionPane.showMessageDialog(this, "Title: " + title + "\nAuthor: " + author + "\nName: " + name + "\nBorrow Date: " + borrowDate + "\nReturn Date: " + returnDate);
        }
    }//GEN-LAST:event_jTableBorrowedMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxIndex;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableAvailable;
    private javax.swing.JTable jTableBorrowed;
    private javax.swing.JButton jbBorrow;
    private javax.swing.JButton jbLogin;
    private javax.swing.JButton jbReturnBook;
    private javax.swing.JButton jbSearch;
    private javax.swing.JPanel jpBorrow;
    private javax.swing.JPanel jpBorrow1;
    private javax.swing.JPanel jpSearch;
    private javax.swing.JTextField jtBorrowDate;
    private javax.swing.JTextField jtBorrowerName;
    private javax.swing.JTextField jtReturnAuthor1;
    private javax.swing.JTextField jtReturnDate;
    private javax.swing.JTextField jtReturnDate1;
    private javax.swing.JTextField jtReturnName1;
    private javax.swing.JTextField jtReturnTittle;
    private javax.swing.JTextField jtSearchAuthor;
    private javax.swing.JTextField jtSearchTittle;
    // End of variables declaration//GEN-END:variables

   
}
