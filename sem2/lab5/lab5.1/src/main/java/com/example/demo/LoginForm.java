package com.example.demo;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
public class LoginForm extends JFrame {
private JTextField usernameField;
private JPasswordField passwordField;
private JButton loginButton;
public LoginForm() {
// UI компоненты
setTitle("Login");
setSize(300, 150);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLayout(new GridLayout(3, 2));
// Поля ввода для логина
usernameField = new JTextField();
passwordField = new JPasswordField();
loginButton = new JButton("Login");
add(new JLabel("Username:"));
add(usernameField);
add(new JLabel("Password:"));
add(passwordField);
add(new JPanel()); // Пустое место
add(loginButton);
// Обработчик авторизации
loginButton.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
// Попытка авторизации
String username = usernameField.getText();
String password = new String(passwordField.getPassword());
String encodedAuth =
Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
// Если авторизация успешна, открываем главную форму
BookForm bookForm = new
BookForm(encodedAuth);
bookForm.setVisible(true);
dispose(); // Закрываем форму авторизации
System.out.println("Encoded Auth: " + encodedAuth);
}
});
}
public static void main(String[] args) {
SwingUtilities.invokeLater(() -> {
new LoginForm().setVisible(true);
});
}
}