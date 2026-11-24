package ru.belgu.cad.lab5.client;

import ru.belgu.cad.lab5.client.model.UserInfo;
import ru.belgu.cad.lab5.client.ui.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Служебный класс: открывает главное окно клиента, делает снимок окна
 * и завершает работу. Используется для проверки отрисовки интерфейса.
 */
public class UiSnapshot {

    public static void main(String[] args) throws Exception {
        String server = args.length > 0 ? args[0] : "http://localhost:8080";
        String login = args.length > 1 ? args[1] : "moderator";
        String password = args.length > 2 ? args[2] : "password";
        String output = args.length > 3 ? args[3] : "lab5_client.png";

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        ApiClient api = new ApiClient(server);
        UserInfo user = api.login(login, password);

        MainFrame[] holder = new MainFrame[1];
        SwingUtilities.invokeAndWait(() -> {
            holder[0] = new MainFrame(api, user);
            holder[0].setVisible(true);
        });
        Thread.sleep(3500);

        Rectangle bounds = holder[0].getBounds();
        BufferedImage image = new BufferedImage(bounds.width, bounds.height,
                BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D graphics = image.createGraphics();
        holder[0].paintAll(graphics);
        graphics.dispose();
        ImageIO.write(image, "png", new File(output));
        System.out.printf("Снимок окна сохранён: %s (%dx%d)%n",
                output, image.getWidth(), image.getHeight());
        System.exit(0);
    }
}
