package com.musicstore.repository;

import com.musicstore.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    
    List<Equipment> findByTypeContainingIgnoreCase(String type);
    
    List<Equipment> findByBrandContainingIgnoreCase(String brand);
    
    List<Equipment> findByNameContainingIgnoreCase(String name);
    
    List<Equipment> findByCategoryId(Long categoryId);
    
    @Query("SELECT e FROM Equipment e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(e.type) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(e.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Equipment> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(e) FROM Equipment e")
    Long countAllEquipment();
    
    @Query("SELECT SUM(e.quantity) FROM Equipment e")
    Long sumAllQuantities();
    
    @Query("SELECT SUM(e.price * e.quantity) FROM Equipment e")
    Double calculateTotalValue();
    
    @Query("SELECT e FROM Equipment e ORDER BY e.addedDate DESC")
    List<Equipment> findAllOrderByDateDesc();
}