package com.example.order_service.service;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.CategorySalesReportDTO;
import com.example.order_service.dto.ProductSalesReportDTO;
import com.example.order_service.exception.SalesReportNotFoundException;
import com.example.order_service.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesReportService {

    private final OrderRepository orderRepository;

    public SalesReportService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<ApiResponse<List<ProductSalesReportDTO>>> getSalesReportByProduct() {

        List<ProductSalesReportDTO> productSalesReports = orderRepository.getAllProductSales();

        if (productSalesReports.isEmpty()){
            throw new SalesReportNotFoundException("No Results");
        }

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched sales report",
                productSalesReports
        ));


    }

    public ResponseEntity<ApiResponse<List<CategorySalesReportDTO>>> getSalesReportByCategory() {

        List<CategorySalesReportDTO> categorySalesReports = orderRepository.getAllCategorySales();

        if (categorySalesReports.isEmpty()){
            throw new SalesReportNotFoundException("No Results");
        }

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched sales report",
                categorySalesReports
        ));

    }
}
