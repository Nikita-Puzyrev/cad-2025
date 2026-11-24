import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaskManagerForm extends JFrame {

    private JList<String> taskList;
    private DefaultListModel<String> taskListModel;
    private String authHeader;

    public TaskManagerForm(String authHeader) {
        this.authHeader = authHeader;
        setTitle("Task Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Список задач
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        loadTasks(); // Загрузка задач из API
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Обновить задачи");
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчик события для кнопки обновления
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

                // Парсинг JSON вручную
                parseJson(jsonResponse.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки задач");
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

        String[] tasksArray = json.split("\\},\\{");
        int taskNumber = 1;
        taskListModel.clear();

        for (String task : tasksArray) {
            task = task.replace("{", "").replace("}", "").trim();

            String description = extractField(task, "description");
            String category = extractField(task, "category");
            String priority = extractField(task, "priority");

            if (description != null) {
                String cat = (category != null && !category.isEmpty()) ? category : "Без категории";
                String prio = (priority != null && !priority.isEmpty()) ? priority : "NORMAL";

                taskListModel.addElement("Задача " + taskNumber++ + ": " + description +
                        " [Категория: " + cat + ", Приоритет: " + prio + "]");
            }
        }
    }

    private String extractField(String task, String fieldName) {
        // В методичке были неверные разделители, здесь исправлено под JSON
        String[] fields = task.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", ""); // Убираем кавычки
                String value = keyValue[1].trim().replace("\"", ""); // Убираем кавычки
                if (key.equals(fieldName)) {
                    return value; // Возвращаем значение поля
                }
            }
        }
        return null; // Если поле не найдено
    }
}