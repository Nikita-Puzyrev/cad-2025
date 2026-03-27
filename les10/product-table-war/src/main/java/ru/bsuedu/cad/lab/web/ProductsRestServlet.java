package ru.bsuedu.cad.lab.web;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import ru.bsuedu.cad.lab.config.HibernateUtil;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.entity.Product;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/products")
public class ProductsRestServlet extends HttpServlet {

    private ProductRepository repo;

    @Override
    public void init() {
        repo = new ProductRepository(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        List<Product> products = repo.findAll();

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        out.println("[");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);

            out.println("{");
            out.println("\"name\":\"" + p.getName() + "\",");
            out.println("\"category\":\"" + p.getCategory().getName() + "\",");
            out.println("\"stock\":" + p.getStockQuantity());
            out.println("}");

            if (i < products.size() - 1) out.println(",");
        }
        out.println("]");
    }
}