package com.example.order_service.service;

import com.example.order_service.exception.*;
import com.example.order_service.dto.*;
import com.example.order_service.feign.ProductClient;
import com.example.order_service.feign.VariantClient;
import com.example.order_service.model.Customer;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderItem;
import com.example.order_service.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final ProductClient productClient;
    private final VariantClient variantClient;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductClient productClient, VariantClient variantClient) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productClient = productClient;
        this.variantClient = variantClient;
    }

    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(CreateOrderRequest createOrderRequest) {

        Optional<Customer> customerOpt = customerRepository.findById(createOrderRequest.getCustomerId());
        if (customerOpt.isEmpty()) {
            throw new CustomerNotFoundException(createOrderRequest.getCustomerId());
        }
        Customer customer = customerOpt.get();

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemsRequest orderItemsRequest : createOrderRequest.getItems()){

            VariantResponseDTO variant;

            try {
                ApiResponse<VariantResponseDTO> variantResponse = variantClient.getVariantById(orderItemsRequest.getVariantId());
                variant = variantResponse.getData();

                if (variant == null) {
                    throw new VariantNotFoundException(orderItemsRequest.getVariantId());
                }
            } catch (FeignException.NotFound e) {
                throw new VariantNotFoundException(orderItemsRequest.getVariantId());
            }

            ProductResponseDTO product;
            try {
                ApiResponse<ProductResponseDTO> productResponse = productClient.getProductById(variant.getProductId());
                product = productResponse.getData();

                if (product == null) {
                    throw new ProductNotFoundException(variant.getProductId());
                }
            } catch (FeignException.NotFound e) {
                throw new ProductNotFoundException(variant.getProductId());
            }

            if (variant.getQuantity() < orderItemsRequest.getQuantity()) {
                throw new InsufficentStockException(variant.getVariantId());
            }

            variantClient.updateStock(variant.getVariantId(), orderItemsRequest.getQuantity());

            double totalPrice = variant.getPrice() * orderItemsRequest.getQuantity();

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    variant.getVariantId(),
                    product.getName(),
                    product.getCategory(),
                    variant.getAttributes(),
                    orderItemsRequest.getQuantity(),
                    variant.getPrice(),
                    totalPrice
            );

            orderItems.add(orderItem);
            totalAmount += totalPrice;
        }

        Order order = new Order(createOrderRequest.getCustomerId(),
                                customer.getName(),
                                orderItems,
                                totalAmount);

        Order savedOrder = orderRepository.save(order);

        List<OrderItemResponseDTO> orderItemResponseDTOs = savedOrder.getItems().stream()
                                                            .map(item -> new OrderItemResponseDTO(
                                                                    item.getProductId(),
                                                                    item.getProductName(),
                                                                    item.getVariantId(),
                                                                    item.getAttributes(),
                                                                    item.getQty(),
                                                                    item.getPrice(),
                                                                    item.getCategory(),
                                                                    item.getTotalPrice())
                                                              ).collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse(savedOrder.getId(),
                                                        customer.getName(),
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

        Optional<Customer> customerOpt = customerRepository.findById(order.getCustomerId());
        if (customerOpt.isEmpty()) {
            throw new CustomerNotFoundException(order.getCustomerId());
        }
        Customer customer = customerOpt.get();

        List<OrderItemResponseDTO> orderItems = order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getProductId(),
                        item.getProductName(),
                        item.getVariantId(),
                        item.getAttributes(),
                        item.getQty(),
                        item.getPrice(),
                        item.getCategory(),
                        item.getTotalPrice()
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
            throw new OrdersNotFoundException(ErrorMessage.NO_ORDERS);
        }

       Page<OrderResponse> orderResponses = orders.map(order ->{
               List<OrderItemResponseDTO> orderItems = order.getItems().stream()
                       .map(item ->
                       new OrderItemResponseDTO(  item.getProductId(),
                               item.getProductName(),
                               item.getVariantId(),
                               item.getAttributes(),
                               item.getQty(),
                               item.getPrice(),
                               item.getCategory(),
                               item.getTotalPrice()))
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
