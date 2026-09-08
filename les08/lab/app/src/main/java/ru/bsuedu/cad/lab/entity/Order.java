package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String status;

    @Column(name = "shipping_address", length = 1000)
    private String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> details = new ArrayList<>();

    public Order() {
    }

    public Order(Customer customer, LocalDateTime orderDate, String status, String shippingAddress) {
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.totalPrice = BigDecimal.ZERO;
    }

    public void addDetail(OrderDetail detail) {
        details.add(detail);
        detail.setOrder(this);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public Customer getCustomer() {
        return customer;
    }
}