package com.example.product_service.repository;

import com.example.product_service.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("{'category': {$regex: ?0, $options: 'i'}}")
    Page<Product> findByCategory(String category, Pageable pageable);
}
