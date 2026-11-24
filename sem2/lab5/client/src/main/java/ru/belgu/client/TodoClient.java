package ru.belgu.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class TodoClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoClient::showLogin);
    }

    private static void showLogin() {
        JFrame frame = new JFrame("Вход в список задач");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        JTextField server = new JTextField(System.getProperty("todo.url", "http://localhost:8080"));
        JTextField username = new JTextField("user");
        JPasswordField password = new JPasswordField();
        JButton login = new JButton("Войти");
        panel.add(new JLabel("Сервер:")); panel.add(server);
        panel.add(new JLabel("Логин:")); panel.add(username);
        panel.add(new JLabel("Пароль:")); panel.add(password);
        panel.add(new JLabel("")); panel.add(login);
        frame.add(panel);
        frame.setSize(480, 210);
        frame.setLocationRelativeTo(null);
        login.addActionListener(event -> {
            TaskApiClient api = new TaskApiClient(server.getText().trim(), username.getText().trim(),
                    new String(password.getPassword()));
            login.setEnabled(false);
            run(frame, api::list, tasks -> {
                frame.dispose();
                showTasks(api, tasks, username.getText().trim());
            }, () -> login.setEnabled(true));
        });
        frame.getRootPane().setDefaultButton(login);
        frame.setVisible(true);
    }

    private static void showTasks(TaskApiClient api, List<TaskItem> initialTasks, String username) {
        JFrame frame = new JFrame("Список задач — " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Описание", "Выполнена", "Приоритет", "Создана (UTC)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0 -> Long.class;
                    case 2 -> Boolean.class;
                    case 3 -> Integer.class;
                    default -> String.class;
                };
            }
        };
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(new JLabel("Сортировка — нажатием на заголовок столбца. Приоритет: 1 высокий, 3 низкий."), BorderLayout.NORTH);
        JTextField description = new JTextField(25);
        JSpinner priority = new JSpinner(new SpinnerNumberModel(2, 1, 3, 1));
        JButton add = new JButton("Добавить");
        JButton delete = new JButton("Удалить выбранную");
        JButton refresh = new JButton("Обновить");
        JPanel panel = new JPanel();
        panel.add(new JLabel("Описание:")); panel.add(description);
        panel.add(new JLabel("Приоритет:")); panel.add(priority);
        panel.add(add); panel.add(delete); panel.add(refresh);
        frame.add(panel, BorderLayout.SOUTH);
        Consumer<List<TaskItem>> display = tasks -> {
            model.setRowCount(0);
            for (TaskItem task : tasks) {
                model.addRow(new Object[]{task.id(), task.description(), task.completed(), task.priority(), task.createdAt()});
            }
        };
        Consumer<Boolean> busy = value -> {
            add.setEnabled(!value); delete.setEnabled(!value); refresh.setEnabled(!value);
        };
        Runnable done = () -> busy.accept(false);
        refresh.addActionListener(event -> {
            busy.accept(true);
            run(frame, api::list, display, done);
        });
        add.addActionListener(event -> {
            String text = description.getText().trim();
            if (text.isEmpty() || text.length() > 500) {
                JOptionPane.showMessageDialog(frame, "Описание должно содержать от 1 до 500 символов");
                return;
            }
            int selectedPriority = (Integer) priority.getValue();
            busy.accept(true);
            run(frame, () -> {
                api.add(text, selectedPriority);
                return api.list();
            }, tasks -> { display.accept(tasks); description.setText(""); }, done);
        });
        delete.addActionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(frame, "Сначала выберите задачу");
                return;
            }
            long id = (Long) model.getValueAt(table.convertRowIndexToModel(selectedRow), 0);
            busy.accept(true);
            run(frame, () -> { api.delete(id); return api.list(); }, display, done);
        });
        display.accept(initialTasks);
        frame.setSize(1050, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static <T> void run(JFrame frame, Callable<T> action, Consumer<T> success, Runnable done) {
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception { return action.call(); }
            @Override
            protected void done() {
                try {
                    success.accept(get());
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                    JOptionPane.showMessageDialog(frame, cause.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                } finally {
                    done.run();
                }
            }
        }.execute();
    }
}
