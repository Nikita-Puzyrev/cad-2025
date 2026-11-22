package ru.bsuedu.cad.lab5.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TaskManager {
    private final JFrame frame = new JFrame("Список задач");
    private final CardLayout layout = new CardLayout();
    private final JPanel screens = new JPanel(layout);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Задача", "Статус", "Приоритет", "Категория", "Автор", "Создана"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel(" ");
    private List<TaskApi.Task> tasks = List.of();
    private TaskApi api;
    private boolean busy;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskManager().show());
    }

    private void show() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screens.add(loginPanel(), "login");
        screens.add(tasksPanel(), "tasks");
        frame.setContentPane(screens);
        frame.setSize(840, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel loginPanel() {
        var fields = new JPanel(new GridLayout(0, 2, 8, 8));
        var username = new JTextField(15);
        var password = new JPasswordField(15);
        var login = new JButton("Войти");
        fields.add(new JLabel("Логин"));
        fields.add(username);
        fields.add(new JLabel("Пароль"));
        fields.add(password);
        fields.add(new JLabel());
        fields.add(login);
        login.addActionListener(event -> {
            if (busy) {
                return;
            }
            char[] chars = password.getPassword();
            var credentials = new TaskApi(username.getText().strip(), chars);
            Arrays.fill(chars, '\0');
            password.setText("");
            run(credentials::list, result -> {
                api = credentials;
                showTasks(result);
                layout.show(screens, "tasks");
            });
        });
        password.addActionListener(event -> login.doClick());
        var panel = new JPanel(new java.awt.GridBagLayout());
        panel.add(fields);
        return panel;
    }

    private JPanel tasksPanel() {
        var panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        var tools = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 6, 0));
        tools.add(button("Обновить", () -> run(api::list, this::showTasks)));
        tools.add(button("Добавить", this::add));
        tools.add(button("Удалить", this::delete));
        tools.add(button("Подробнее", this::details));
        tools.add(button("Мои входы", this::logins));
        tools.add(button("Выйти", () -> {
            api = null;
            showTasks(List.of());
            layout.show(screens, "login");
        }));
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(tools, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);
        return panel;
    }

    private JButton button(String title, Runnable action) {
        var button = new JButton(title);
        button.addActionListener(event -> {
            if (!busy) {
                action.run();
            }
        });
        return button;
    }

    private void showTasks(List<TaskApi.Task> result) {
        tasks = result;
        model.setRowCount(0);
        for (var task : result) {
            model.addRow(new Object[]{task.title(), state(task.completed()), task.priority(), task.category(),
                    task.owner(), task.createdAt()});
        }
        status.setText("Задач: " + result.size());
    }

    private void add() {
        var title = new JTextField(20);
        var description = new JTextField(20);
        var priority = new JComboBox<>(new String[]{"LOW", "NORMAL", "HIGH"});
        priority.setSelectedItem("NORMAL");
        var category = new JTextField(20);
        Object[] fields = {"Название", title, "Описание", description, "Приоритет", priority, "Категория", category};
        if (JOptionPane.showConfirmDialog(frame, fields, "Новая задача", JOptionPane.OK_CANCEL_OPTION)
                == JOptionPane.OK_OPTION) {
            var task = new TaskApi.NewTask(title.getText(), description.getText(),
                    (String) priority.getSelectedItem(), category.getText());
            run(() -> {
                api.create(task);
                return api.list();
            }, this::showTasks);
        }
    }

    private void delete() {
        var task = selectedTask();
        if (task != null && JOptionPane.showConfirmDialog(frame, "Удалить «" + task.title() + "»?",
                "Удаление", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            run(() -> {
                api.delete(task.id());
                return api.list();
            }, this::showTasks);
        }
    }

    private void details() {
        var task = selectedTask();
        if (task != null) {
            run(() -> api.details(task.id()), details -> {
                var text = new StringBuilder(details.task().description()).append("\n\nПодзадачи\n");
                details.subtasks().forEach(sub -> text.append(sub.description()).append(" — ")
                        .append(state(sub.completed())).append('\n'));
                text.append("\nИстория изменений\n");
                details.changes().forEach(change -> {
                    text.append(change.changedAt()).append('\n');
                    if (change.oldState() != null) {
                        text.append("До: ").append(change.oldState()).append('\n');
                    }
                    text.append("После: ").append(change.newState()).append("\n\n");
                });
                showText(details.task().title(), text.toString());
            });
        }
    }

    private void logins() {
        run(api::loginHistory, result -> {
            var text = new StringBuilder();
            result.forEach(login -> text.append(login.loggedAt()).append("   ").append(login.ipAddress()).append('\n'));
            showText("Мои входы", text.toString());
        });
    }

    private TaskApi.Task selectedTask() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Выберите задачу.");
            return null;
        }
        return tasks.get(table.convertRowIndexToModel(row));
    }

    private void showText(String title, String text) {
        var area = new JTextArea(text, 12, 48);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setCaretPosition(0);
        JOptionPane.showMessageDialog(frame, new JScrollPane(area), title, JOptionPane.INFORMATION_MESSAGE);
    }

    private String state(boolean completed) {
        return completed ? "Выполнена" : "В работе";
    }

    private <T> void run(Callable<T> work, Consumer<T> onSuccess) {
        busy = true;
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return work.call();
            }

            @Override
            protected void done() {
                busy = false;
                frame.setCursor(Cursor.getDefaultCursor());
                try {
                    onSuccess.accept(get());
                } catch (ExecutionException error) {
                    var cause = error.getCause();
                    String message = cause.getMessage() == null ? "Сервер недоступен" : cause.getMessage();
                    JOptionPane.showMessageDialog(frame, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException error) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }
}
