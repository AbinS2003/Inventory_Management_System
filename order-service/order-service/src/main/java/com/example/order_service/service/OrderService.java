package com.example.order_service.service;

import com.example.order_service.exception.*;
import com.example.order_service.dto.*;
import com.example.order_service.feign.ProductClient;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderItem;
import com.example.order_service.repository.OrderRepository;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderService(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(CreateOrderRequest createOrderRequest) {


        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemsRequest orderItemsRequest : createOrderRequest.getItems()){

            ProductResponseDTO product;

            try {

                ApiResponse<ProductResponseDTO> response = productClient.getProductById(orderItemsRequest.getProductId());
                product = response.getData();

                if (product == null) {
                    throw new ProductNotFoundException(orderItemsRequest.getProductId());
                }


                if (product.getQuantity() < orderItemsRequest.getQuantity()) {
                    throw new InsufficentStockException(orderItemsRequest.getProductId());
                }

            productClient.updateStock(product.getId(), orderItemsRequest.getQuantity());

            } catch (FeignException.NotFound e){
                throw new ProductNotFoundException(orderItemsRequest.getProductId());
            }

            double totalPrice = product.getPrice() * orderItemsRequest.getQuantity();

            OrderItem orderItem = new OrderItem(product.getId(),
                                                product.getName(),
                                                orderItemsRequest.getQuantity(),
                                                product.getPrice(),
                                                product.getCategory(),
                                                totalPrice);

            orderItems.add(orderItem);
            totalAmount += totalPrice;
        }

        Order order = new Order(createOrderRequest.getCustomerName(),
                                orderItems,
                                totalAmount);

        Order savedOrder = orderRepository.save(order);

        List<OrderItemResponseDTO> orderItemResponseDTOs = savedOrder.getItems().stream()
                                                            .map(item -> new OrderItemResponseDTO(
                                                                    item.getProductId(),
                                                                    item.getProductName(),
                                                                    item.getQty(),
                                                                    item.getPrice(),
                                                                    item.getCategory(),
                                                                    item.getTotalPrice())
                                                              ).collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse(savedOrder.getId(),
                                                        savedOrder.getCustomerName(),
                                                        orderItemResponseDTOs,
                                                        savedOrder.getTotalAmount());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Order created",
                orderResponse
        ));

    }

    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(String id) {

        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()){
            throw new OrderNotFoundException(id);
        }

        Order order = orderOpt.get();

        List<OrderItemResponseDTO> orderItems = order.getItems().stream()
                .map(orderItem -> new OrderItemResponseDTO(
                                orderItem.getProductId(),
                                orderItem.getProductName(),
                                orderItem.getQty(),
                                orderItem.getPrice(),
                                orderItem.getCategory(),
                                orderItem.getTotalPrice()
                                ))
                                .collect(Collectors.toList());


        OrderResponse orderResponse = new OrderResponse(order.getId(),
                order.getCustomerName(),
                orderItems,
                order.getTotalAmount());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Order fetched",
                orderResponse
        ));
    }

    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Order> orders = orderRepository.findAll(pageable);

        if (orders.isEmpty()){
            throw new OrdersNotFoundException("No Orders");
        }

       Page<OrderResponse> orderResponses = orders.map(order ->{
               List<OrderItemResponseDTO> orderItems = order.getItems().stream()
                       .map(orderItem ->
                       new OrderItemResponseDTO(orderItem.getProductId(),
                                                   orderItem.getProductName(),
                                                   orderItem.getQty(),
                                                   orderItem.getPrice(),
                                                   orderItem.getCategory(),
                                                   orderItem.getTotalPrice()))
                                                    .collect(Collectors.toList());

               return new OrderResponse(order.getId(),
                       order.getCustomerName(),
                       orderItems,
                       order.getTotalAmount());
       });

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched all orders",
                orderResponses
        ));

    }

}
