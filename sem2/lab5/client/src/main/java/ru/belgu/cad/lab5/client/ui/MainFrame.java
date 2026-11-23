package ru.belgu.cad.lab5.client.ui;

import ru.belgu.cad.lab5.client.ApiClient;
import ru.belgu.cad.lab5.client.model.TaskItem;
import ru.belgu.cad.lab5.client.model.UserInfo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * Главное окно клиента: таблица задач из базы данных, добавление, удаление
 * (только для модератора и администратора) и сортировка.
 */
public class MainFrame extends JFrame {

    private final ApiClient api;
    private final UserInfo user;

    private final TaskTableModel model = new TaskTableModel();
    private final JTable table = new JTable(model);
    private final JButton refreshButton = new JButton("Обновить");
    private final JButton addButton = new JButton("Добавить");
    private final JButton deleteButton = new JButton("Удалить");
    private final JComboBox<String> sortBox = new JComboBox<>(new String[]{
            "по времени создания", "по приоритету", "по категории",
            "по названию", "по статусу"});
    private final JComboBox<String> directionBox =
            new JComboBox<>(new String[]{"по возрастанию", "по убыванию"});
    private final JLabel statusLabel = new JLabel(" ");

    public MainFrame(ApiClient api, UserInfo user) {
        super("Список задач — " + user.username() + " (" + String.join(", ",
                user.roles()) + ")");
        this.api = api;
        this.user = user;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 560);
        setLocationRelativeTo(null);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);

        addButton.setEnabled(user.canCreate());
        deleteButton.setEnabled(false);

        refreshButton.addActionListener(e -> reload());
        addButton.addActionListener(e -> openAddDialog());
        deleteButton.addActionListener(e -> deleteSelected());
        table.getSelectionModel().addListSelectionListener(e -> updateButtons());
        sortBox.addActionListener(e -> reload());
        directionBox.addActionListener(e -> reload());

        setContentPane(buildContent());
        reload();
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(0, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel title = new JLabel("Задачи");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        toolbar.add(title);
        toolbar.add(refreshButton);
        toolbar.add(addButton);
        toolbar.add(deleteButton);
        toolbar.add(new JLabel("   Сортировка:"));
        toolbar.add(sortBox);
        toolbar.add(directionBox);

        root.add(toolbar, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        root.add(statusLabel, BorderLayout.SOUTH);
        return root;
    }

    private String sortKey() {
        return switch (sortBox.getSelectedIndex()) {
            case 1 -> "priority";
            case 2 -> "category";
            case 3 -> "title";
            case 4 -> "status";
            default -> "created";
        };
    }

    private void reload() {
        String direction = directionBox.getSelectedIndex() == 1 ? "desc" : "asc";
        new Thread(() -> {
            try {
                List<TaskItem> tasks = api.list(sortKey(), direction);
                SwingUtilities.invokeLater(() -> {
                    model.setTasks(tasks);
                    statusLabel.setText("Загружено задач: " + tasks.size());
                    updateButtons();
                });
            } catch (Exception ex) {
                showError("Не удалось получить список задач", ex);
            }
        }, "reload").start();
    }

    private void openAddDialog() {
        AddTaskDialog dialog = new AddTaskDialog(this);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            new Thread(() -> {
                try {
                    api.create(dialog.getTitleText(), dialog.getDescriptionText(),
                            dialog.getCategoryText(), dialog.getPriorityText());
                    SwingUtilities.invokeLater(this::reload);
                } catch (Exception ex) {
                    showError("Не удалось добавить задачу", ex);
                }
            }, "add").start();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        TaskItem task = model.get(table.convertRowIndexToModel(row));
        int answer = JOptionPane.showConfirmDialog(this,
                "Удалить задачу «" + task.title() + "»?", "Подтверждение",
                JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        new Thread(() -> {
            try {
                api.delete(task.id());
                SwingUtilities.invokeLater(this::reload);
            } catch (Exception ex) {
                showError("Не удалось удалить задачу", ex);
            }
        }, "delete").start();
    }

    private void updateButtons() {
        deleteButton.setEnabled(user.canDelete() && table.getSelectedRow() >= 0);
    }

    private void showError(String title, Exception ex) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                title, JOptionPane.ERROR_MESSAGE));
    }

    /** Модель таблицы: колонки соответствуют полям задачи, включая данные из БД. */
    static class TaskTableModel extends AbstractTableModel {

        private static final String[] COLUMNS = {
                "№", "Название", "Описание", "Категория", "Приоритет",
                "Создана", "Автор", "Подзадачи", "Статус"};

        private List<TaskItem> tasks = new ArrayList<>();

        void setTasks(List<TaskItem> tasks) {
            this.tasks = tasks;
            fireTableDataChanged();
        }

        TaskItem get(int row) {
            return tasks.get(row);
        }

        @Override
        public int getRowCount() {
            return tasks.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            TaskItem task = tasks.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> rowIndex + 1;
                case 1 -> task.title();
                case 2 -> task.description();
                case 3 -> task.category() == null ? "—" : task.category();
                case 4 -> task.priority();
                case 5 -> task.createdAt();
                case 6 -> task.author();
                case 7 -> task.subTasksAsText();
                case 8 -> task.statusText();
                default -> "";
            };
        }
    }
}
