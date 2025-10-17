package com.example.order_service.controller;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.CategorySalesReportDTO;
import com.example.order_service.dto.ProductSalesReportDTO;
import com.example.order_service.service.SalesReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/reports")
@Tag(name = "Sales-Report controller",
        description = "Handles sales report operations, including total sales by product and category")
public class SalesReportController {

    private final SalesReportService salesReportService;

    public SalesReportController(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

    @GetMapping("/product")
    @Operation(
            summary = "Get Sales Report by Product",
            description = "Returns a list of products with total quantity sold and total sales amount across all orders."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sales report fetched succesfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Report not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Inernal server error")
    })
    public ResponseEntity<ApiResponse<List<ProductSalesReportDTO>>> getSalesReportByProduct(){

        return salesReportService.getSalesReportByProduct();
    }

    @GetMapping("/category")
    @Operation(
            summary = "Get Sales Report by Category",
            description = "Returns a list of category with total quantity sold and total sales amount across all orders."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sales report fetched succesfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Report not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Inernal server error")
    })
    public ResponseEntity<ApiResponse<List<CategorySalesReportDTO>>> getSalesReportByCategory(){

        return salesReportService.getSalesReportByCategory();
    }

}
