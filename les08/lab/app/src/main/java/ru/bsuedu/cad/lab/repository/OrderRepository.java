package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bsuedu.cad.lab.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("""
            SELECT DISTINCT o
            FROM Order o
            LEFT JOIN FETCH o.details d
            LEFT JOIN FETCH d.product
            LEFT JOIN FETCH o.customer
            """)
    List<Order> findAllWithDetails();
}