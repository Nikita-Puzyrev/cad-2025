package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CreateOrderServlet extends HttpServlet {

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

        var out = response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Создание заказа</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 30px; }
                        form { width: 400px; }
                        label { display: block; margin-top: 12px; }
                        input { width: 100%; padding: 8px; margin-top: 4px; }
                        button { margin-top: 16px; padding: 10px 14px; }
                    </style>
                </head>
                <body>
                    <h1>Создание заказа</h1>

                    <form method="post" action="new">
                        <label>Id покупателя</label>
                        <input type="number" name="customerId" value="1" required>

                        <label>Id товара</label>
                        <input type="number" name="productId" value="1" required>

                        <label>Количество</label>
                        <input type="number" name="quantity" value="1" required>

                        <button type="submit">Создать заказ</button>
                    </form>

                    <p><a href="../orders">Вернуться к списку заказов</a></p>
                </body>
                </html>
                """);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Integer customerId = Integer.parseInt(request.getParameter("customerId"));
        Integer productId = Integer.parseInt(request.getParameter("productId"));
        Integer quantity = Integer.parseInt(request.getParameter("quantity"));

        orderService.createOrder(customerId, Map.of(productId, quantity));

        response.sendRedirect(request.getContextPath() + "/orders");
    }
}