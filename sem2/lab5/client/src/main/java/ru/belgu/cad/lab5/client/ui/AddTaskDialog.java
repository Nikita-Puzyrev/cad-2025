package ru.belgu.cad.lab5.client.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/** Диалог добавления задачи: название, описание, категория, приоритет. */
public class AddTaskDialog extends JDialog {

    private final JTextField titleField = new JTextField(24);
    private final JTextArea descriptionArea = new JTextArea(3, 24);
    private final JTextField categoryField = new JTextField(18);
    private final JComboBox<String> priorityBox =
            new JComboBox<>(new String[]{"HIGH", "NORMAL", "LOW"});

    private boolean confirmed;

    public AddTaskDialog(Frame owner) {
        super(owner, "Новая задача", true);
        setContentPane(buildContent());
        pack();
        setLocationRelativeTo(owner);
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 0, 4, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        form.add(new JLabel("Название:"), c);
        c.gridx = 1;
        form.add(titleField, c);

        c.gridx = 0;
        c.gridy = 1;
        form.add(new JLabel("Описание:"), c);
        c.gridx = 1;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        form.add(new JScrollPane(descriptionArea), c);

        c.gridx = 0;
        c.gridy = 2;
        form.add(new JLabel("Категория:"), c);
        c.gridx = 1;
        form.add(categoryField, c);

        c.gridx = 0;
        c.gridy = 3;
        form.add(new JLabel("Приоритет:"), c);
        c.gridx = 1;
        form.add(priorityBox, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton ok = new JButton("Добавить");
        JButton cancel = new JButton("Отмена");
        ok.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancel.addActionListener(e -> dispose());
        buttons.add(cancel);
        buttons.add(ok);

        root.add(form, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(ok);
        return root;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getTitleText() {
        return titleField.getText().trim();
    }

    public String getDescriptionText() {
        return descriptionArea.getText().trim();
    }

    public String getCategoryText() {
        return categoryField.getText().trim();
    }

    public String getPriorityText() {
        return (String) priorityBox.getSelectedItem();
    }
}
