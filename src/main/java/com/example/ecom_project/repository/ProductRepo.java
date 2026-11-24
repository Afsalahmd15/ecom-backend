package com.example.ecom_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ecom_project.model.Product;

@Repository 
public interface ProductRepo extends JpaRepository<Product, Integer> {
    
    @Query("SELECT p FROM Product p WHERE "+
    "LOWER(p.name) LIKE LOWER(CONCAT('%', :name ,'%')) OR "+
    "LOWER(p.description) LIKE LOWER(CONCAT('%', :name, '%')) OR "+
    "LOWER(p.brand) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
    "LOWER(p.category) LIKE LOWER(CONCAT('%', :name, '%'))"

    )
    List<Product> searchProduct(String name);
}
