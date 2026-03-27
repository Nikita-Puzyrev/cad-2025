package ru.bsuedu.cad.lab.web;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.hibernate.SessionFactory;
import ru.bsuedu.cad.lab.config.HibernateUtil;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {

    private OrderService service;

    @Override
    public void init() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        OrderRepository orderRepo = new OrderRepository(factory);
        CustomerRepository customerRepo = new CustomerRepository(factory);
        ProductRepository productRepo = new ProductRepository(factory);

        service = new OrderService(orderRepo, customerRepo, productRepo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Create Order</h1>");
        out.println("<form method='post'>");
        out.println("Status: <input name='status'/><br>");
        out.println("Total: <input type='number' step='0.01' name='total' required/><br>");
        out.println("<button type='submit'>Create</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String status = req.getParameter("status");
        String totalStr = req.getParameter("total");

        if (status == null || status.isBlank() ||
                totalStr == null || totalStr.isBlank()) {

            resp.getWriter().println("Invalid input!");
            return;
        }

        BigDecimal total;
        try {
            total = new BigDecimal(totalStr);
        } catch (NumberFormatException e) {
            resp.getWriter().println("Total must be a number!");
            return;
        }

        Order order = new Order();
        order.setStatus(status);
        order.setTotalPrice(total);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress("Web");

        service.createOrder(order);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress("Web");

        service.createOrder(order);

        resp.sendRedirect("orders");
    }
}