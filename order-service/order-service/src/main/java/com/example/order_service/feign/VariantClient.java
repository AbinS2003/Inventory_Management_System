package com.example.order_service.feign;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.ProductResponseDTO;
import com.example.order_service.dto.VariantResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", contextId = "variantClient", url = "http://localhost:8081/api/variants")
public interface VariantClient {

    @GetMapping("{id}")
    public ApiResponse<VariantResponseDTO> getVariantById(@PathVariable("id") String variantId);

    @PutMapping("/reduce/{id}")
    public ApiResponse<VariantResponseDTO> updateStock(@PathVariable("id") String id,
                                                       @RequestParam("quantity") int quantity);


}
