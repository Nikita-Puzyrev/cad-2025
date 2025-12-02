package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserLogin(String userLogin);
    List<Rental> findByIsActiveTrue();
    List<Rental> findByCarId(Long carId);
}