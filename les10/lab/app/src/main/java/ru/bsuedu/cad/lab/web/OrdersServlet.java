package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OrdersServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        List<Order> orders = orderService.getAllOrders();

        var out = response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Заказы</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 30px; }
                        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
                        th, td { border: 1px solid #999; padding: 8px; text-align: left; }
                        th { background: #eee; }
                        a.button {
                            display: inline-block;
                            padding: 10px 14px;
                            background: #2d7ef7;
                            color: white;
                            text-decoration: none;
                            border-radius: 4px;
                        }
                    </style>
                </head>
                <body>
                    <h1>Список заказов</h1>
                    <a class="button" href="orders/new">Создать заказ</a>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Покупатель</th>
                                <th>Дата</th>
                                <th>Статус</th>
                                <th>Сумма</th>
                                <th>Адрес доставки</th>
                                <th>Позиций</th>
                            </tr>
                        </thead>
                        <tbody>
                """);

        for (Order order : orders) {
            out.printf("""
                            <tr>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%d</td>
                            </tr>
                    """,
                    order.getOrderId(),
                    escape(order.getCustomer().getName()),
                    order.getOrderDate(),
                    escape(order.getStatus()),
                    order.getTotalPrice(),
                    escape(order.getShippingAddress()),
                    order.getDetails().size()
            );
        }

        out.println("""
                        </tbody>
                    </table>
                </body>
                </html>
                """);
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}