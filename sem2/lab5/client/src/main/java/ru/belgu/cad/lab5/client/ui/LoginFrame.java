package ru.belgu.cad.lab5.client.ui;

import ru.belgu.cad.lab5.client.ApiClient;
import ru.belgu.cad.lab5.client.model.UserInfo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/** Форма входа: адрес сервера, логин и пароль. */
public class LoginFrame extends JFrame {

    private final JTextField serverField =
            new JTextField("http://localhost:8080", 24);
    private final JTextField loginField = new JTextField("user", 18);
    private final JPasswordField passwordField = new JPasswordField("password", 18);
    private final JButton loginButton = new JButton("Войти");

    public LoginFrame() {
        super("Список задач — вход");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(buildContent());
        pack();
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(loginButton);
        loginButton.addActionListener(e -> doLogin());
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Вход в приложение");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 0, 4, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        form.add(new JLabel("Сервер:"), c);
        c.gridx = 1;
        form.add(serverField, c);

        c.gridx = 0;
        c.gridy = 1;
        form.add(new JLabel("Логин:"), c);
        c.gridx = 1;
        form.add(loginField, c);

        c.gridx = 0;
        c.gridy = 2;
        form.add(new JLabel("Пароль:"), c);
        c.gridx = 1;
        form.add(passwordField, c);

        root.add(form, BorderLayout.CENTER);
        root.add(loginButton, BorderLayout.SOUTH);
        return root;
    }

    private void doLogin() {
        loginButton.setEnabled(false);
        String server = serverField.getText().trim();
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        new Thread(() -> {
            try {
                ApiClient api = new ApiClient(server);
                UserInfo user = api.login(login, password);
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    new MainFrame(api, user).setVisible(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                            "Ошибка входа", JOptionPane.ERROR_MESSAGE);
                    loginButton.setEnabled(true);
                });
            }
        }, "login").start();
    }
}
