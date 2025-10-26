package com.example.order_service.controller;


import com.example.order_service.dto.*;
import com.example.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@Tag(name = "order controller",
description = "Handle CRUD operations for order-service")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create an order",
    description = "Creates a new order for the specified products. Validates product availability." +
            " calculates total amount, and processes the order. Returns the created order details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Order created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request - missing required fields or invalid data format"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Insufficient stock"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest){

        return orderService.createOrder(createOrderRequest);
    }


    @GetMapping("{id}")
    @Operation(summary = "Fetch an order by id",
            description = "Retrieves the details of an existing order using its unique ID, including customer information," +
                    " ordered items, and total amount.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order fetched succesfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Inernal server error")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById( @Parameter(description = "Unique ID of the order to fetch") @PathVariable String id){

        return orderService.getOrderById(id);
    }

    @GetMapping
    @Operation(summary = "Fetch all orders as pages",
            description = "Retrieves a paginated list of all orders with sorting and filtering options.Returns order details including customer information, ordered items, and total amounts.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders fetched succesfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Orders not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Inernal server error")
    })
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int pageSize){

        return orderService.getAllOrders(page, pageSize);
    }


}
