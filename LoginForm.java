package library;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginForm extends JFrame {
    private JLabel usernameLabel, passwordLabel, messageLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Dashboard dashboard;
    private ArrayList<Book> books;

    public LoginForm() {
        setTitle("STAFF");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 20, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 160, 25);
        add(usernameField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 50, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 50, 160, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 80, 80, 25);
        add(loginButton);

        messageLabel = new JLabel();
        messageLabel.setBounds(20, 110, 250, 25);
        add(messageLabel);

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public void setBooksList(ArrayList<Book> books) {
        this.books = books;
    }

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("1") && password.equals("1")) {
            messageLabel.setText("Login successful");

            ArrayList<BorrowedBook> theLastOne = new ArrayList<>();
            StaffForm staffForm = new StaffForm(books, dashboard, theLastOne);
            staffForm.setVisible(true);
            this.dispose(); 
        } else {
            messageLabel.setText("Login failed");
        }
    }
}
