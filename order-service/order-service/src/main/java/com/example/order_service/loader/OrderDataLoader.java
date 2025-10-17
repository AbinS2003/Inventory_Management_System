package com.example.order_service.loader;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderItemsRequest;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDataLoader implements CommandLineRunner {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderDataLoader(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (orderRepository.count() == 0) {

            List<CreateOrderRequest> orders = List.of(
                    new CreateOrderRequest("Alice", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04ef", 2)
                    )),
                    new CreateOrderRequest("Bob", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f0", 1),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f2", 3)
                    )),
                    new CreateOrderRequest("Charlie", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f1", 1)
                    )),
                    new CreateOrderRequest("David", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f4", 2),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f3", 1)
                    )),
                    new CreateOrderRequest("Eve", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04ef", 1),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f0", 2)
                    )),
                    new CreateOrderRequest("Frank", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f1", 2)
                    )),
                    new CreateOrderRequest("Grace", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f3", 1),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f4", 1)
                    )),
                    new CreateOrderRequest("Hannah", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f4", 1)
                    )),
                    new CreateOrderRequest("Ivy", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04ef", 2),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f1", 1)
                    )),
                    new CreateOrderRequest("Jack", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f0", 1)
                    )),
                    new CreateOrderRequest("Kevin", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f2", 2)
                    )),
                    new CreateOrderRequest("Laura", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f2", 3),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f4", 1)
                    )),
                    new CreateOrderRequest("Mike", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04ef", 1)
                    )),
                    new CreateOrderRequest("Nina", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f0", 2),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f1", 1)
                    )),
                    new CreateOrderRequest("Oscar", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f3", 1)
                    )),
                    new CreateOrderRequest("Pam", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f2", 2),
                            new OrderItemsRequest("68e2a634437d171ea0fc04ef", 1)
                    )),
                    new CreateOrderRequest("Quinn", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f4", 3)
                    )),
                    new CreateOrderRequest("Ryan", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f1", 2),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f0", 1)
                    )),
                    new CreateOrderRequest("Sophia", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04ef", 2)
                    )),
                    new CreateOrderRequest("Tom", List.of(
                            new OrderItemsRequest("68e2a634437d171ea0fc04f2", 1),
                            new OrderItemsRequest("68e2a634437d171ea0fc04f4", 2)
                    ))
            );


            for (CreateOrderRequest createOrderRequest : orders){
                orderService.createOrder(createOrderRequest);
            }

            System.out.println("Order data loaded!!");
        }

    }
}
