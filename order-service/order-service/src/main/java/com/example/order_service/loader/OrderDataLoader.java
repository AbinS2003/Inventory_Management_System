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

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderDataLoader(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {

        if (orderRepository.count() == 0) {

            List<CreateOrderRequest> orders = List.of(

                    new CreateOrderRequest("68f76f1f489852a565f3563b", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b6785d", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67867", 1)
                    )),

                    new CreateOrderRequest("68f76f1f489852a565f3563c", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b6785f", 1)
                    )),

                    new CreateOrderRequest("68f76f1f489852a565f3563d", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b67863", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67865", 2)
                    )),

                    new CreateOrderRequest("68f76f1f489852a565f3563e", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b67861", 3)
                    )),

                    new CreateOrderRequest("68f76f1f489852a565f3563f", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b67863", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67861", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67867", 2)
                    )),

                    new CreateOrderRequest("68f76f20489852a565f35640", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b6785f", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67863", 1)
                    )),

                    new CreateOrderRequest("68f76f1f489852a565f3563b", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b6785d", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67865", 1)
                    )),

                    new CreateOrderRequest("68f76f1f489852a565f3563c", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b67865", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67861", 2)
                    )),

                    new CreateOrderRequest("68f76f1f489852a565f3563d", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b67863", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67865", 1)
                    )),

                    new CreateOrderRequest("68f76f1f489852a565f3563e", List.of(
                            new OrderItemsRequest("68f76c9937d5b9cb69b67863", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67865", 1),
                            new OrderItemsRequest("68f76c9937d5b9cb69b67861", 1)
                    ))
            );


            orders.forEach(orderService::createOrder);

            System.out.println("sample orders with multiple variants created!");
        }
    }
}
