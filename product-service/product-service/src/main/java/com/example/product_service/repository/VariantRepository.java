package com.example.product_service.repository;

import com.example.product_service.model.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends MongoRepository<Variant, String> {

    void deleteByProductId(String id);


    List<Variant> findByProductId(String id);

    List<Variant> findByProductIdIn(List<String> productIds);


}
