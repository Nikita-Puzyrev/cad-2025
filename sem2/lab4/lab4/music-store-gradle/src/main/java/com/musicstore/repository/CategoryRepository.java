package com.musicstore.repository;

import com.musicstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    @Query("SELECT c FROM Category c ORDER BY c.name")
    List<Category> findAllOrderByName();
    
    @Query("SELECT COUNT(e) FROM Equipment e WHERE e.category.id = :categoryId")
    Long countEquipmentByCategoryId(Long categoryId);
}