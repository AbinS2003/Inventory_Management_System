package com.example.order_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.ProductResponseDTO;

@FeignClient(name = "product-service", contextId = "productClient", url = "http://localhost:8081/api/products")
public interface ProductClient {

    @GetMapping("{id}")
    public ApiResponse<ProductResponseDTO> getProductById(@PathVariable("id") String productId);

}
