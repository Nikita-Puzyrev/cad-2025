package ru.bsuedu.cad.lab.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.entity.Order;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {

    private OrderService service = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Order> orders = service.getAllOrders();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Orders</h1>");

        out.println("<a href='create-order'>Create Order</a><br><br>");

        out.println("<table border=1>");
        out.println("<tr><th>ID</th><th>Status</th><th>Total</th></tr>");

        for (Order o : orders) {
            out.println("<tr>");
            out.println("<td>" + o.getId() + "</td>");
            out.println("<td>" + o.getStatus() + "</td>");
            out.println("<td>" + o.getTotalPrice() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</body></html>");
    }
}