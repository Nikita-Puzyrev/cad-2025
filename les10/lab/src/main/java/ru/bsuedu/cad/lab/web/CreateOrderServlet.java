package ru.bsuedu.cad.lab.web;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {

    private OrderService service = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Create Order</h1>");
        out.println("<form method='post'>");
        out.println("Status: <input name='status'/><br>");
        out.println("Total: <input name='total'/><br>");
        out.println("<button type='submit'>Create</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String status = req.getParameter("status");
        String total = req.getParameter("total");

        Order order = new Order();
        order.setStatus(status);
        order.setTotalPrice(new BigDecimal(total));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress("Web");

        service.createOrder(order);

        resp.sendRedirect("orders");
    }
}