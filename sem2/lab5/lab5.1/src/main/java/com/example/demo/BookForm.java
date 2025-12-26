package com.example.demo;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class BookForm extends JFrame {
    private JList<String> taskList;
    private DefaultListModel<String> taskListModel;
    private String authHeader;

    public BookForm(String authHeader) {
        this.authHeader = authHeader;
        setTitle("Book Manager");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Список книг
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Обновить книги");
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Загрузка книг при старте
        loadTasks();

        // Обработчик кнопки обновления
        refreshButton.addActionListener(e -> loadTasks());
    }

    private void loadTasks() {
        try {
            URL url = new URL("http://localhost:8080/api");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + authHeader);

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    jsonResponse.append(inputLine);
                }
                in.close();

                parseJson(jsonResponse.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки книг");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJson(String json) {
    json = json.trim();
    if (json.startsWith("[") && json.endsWith("]")) {
        json = json.substring(1, json.length() - 1);
    }

    String[] booksArray = json.split("\\},\\{");
    taskListModel.clear();
    int bookNumber = 1;

    for (String book : booksArray) {
        book = book.replace("{", "").replace("}", "").trim();
        String title = extractField(book, "title");
        String author = extractField(book, "author");
        String available = extractField(book, "available");
        String read = extractField(book, "read");

        // Вывод в одну строку с пробелами между атрибутами
        String displayText = bookNumber++ + ". "+ "Название:" + " " + title + " " + "Автор: " + author + " " + "Доступна ли? " + available + " " + "Прочитана ли? " + read;
        taskListModel.addElement(displayText);
    }
}

    private String extractField(String book, String fieldName) {
        String[] fields = book.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":", 2); // Важно: 2, чтобы корректно обрабатывать ":" в тексте
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");
                if (key.equals(fieldName)) {
                    return value;
                }
            }
        }
        return "N/A";
    }

    public static void main(String[] args) {
        String authHeader = "dXNlcjpwYXNzd29yZA=="; 
        SwingUtilities.invokeLater(() -> new BookForm(authHeader).setVisible(true));
    }
}
