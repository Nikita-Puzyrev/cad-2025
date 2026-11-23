package ru.belgu.cad.lab5.client;

import ru.belgu.cad.lab5.client.ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Точка входа клиентского приложения на Swing. */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // при недоступности системного стиля используется стандартный
        }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
