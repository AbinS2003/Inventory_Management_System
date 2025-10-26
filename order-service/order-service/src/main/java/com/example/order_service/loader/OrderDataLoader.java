package com.example.order_service.loader;

import com.example.order_service.dto.AddonRequest;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderItemsRequest;
import com.example.order_service.feign.ProductClient;
import com.example.order_service.repository.CustomerRepository;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class OrderDataLoader implements CommandLineRunner {

    private final ProductClient productClient;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderDataLoader(ProductClient productClient, CustomerRepository customerRepository, OrderService orderService, OrderRepository orderRepository) {
        this.productClient = productClient;
        this.customerRepository = customerRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {

        if(orderRepository.count() == 0) {

            String customer1Id = customerRepository.findByName("Alice Johnson").orElseThrow().getId();
            String customer2Id = customerRepository.findByName("Bob Smith").orElseThrow().getId();
            String customer3Id = customerRepository.findByName("Julia Roberts").orElseThrow().getId();
            String customer4Id = customerRepository.findByName("Ethan Hunt").orElseThrow().getId();
            String customer5Id = customerRepository.findByName("Hannah Lee").orElseThrow().getId();

            String laptopVariantId = productClient.getVariantIdsByProductName("Gaming Laptop").get(0);
            String mouseVariantId = productClient.getVariantIdsByProductName("Wireless Mouse").get(0);
            String smartPhoneVariantId = productClient.getVariantIdsByProductName("Smartphone").get(0);
            String phoneCaseVariantId = productClient.getVariantIdsByProductName("phone Case").get(0);
            String monitorVariantId = productClient.getVariantIdsByProductName("LED Monitor").get(0);
            String webcamVariantId = productClient.getVariantIdsByProductName("Webcam").get(0);
            String laptopBagVariantId = productClient.getVariantIdsByProductName("Laptop Bag").get(0);
            String chairVariantId = productClient.getVariantIdsByProductName("Office Chair").get(0);

            OrderItemsRequest laptopOrder = new OrderItemsRequest(
                    laptopVariantId,
                    1,
                    List.of(new AddonRequest(mouseVariantId))
            );

            OrderItemsRequest smartphoneOrder = new OrderItemsRequest(
                    smartPhoneVariantId,
                    1,
                    List.of(new AddonRequest(phoneCaseVariantId))
            );

            OrderItemsRequest monitorOrder = new OrderItemsRequest(
                    monitorVariantId,
                    1,
                    List.of(new AddonRequest(webcamVariantId))
            );

            OrderItemsRequest laptopBagOrder = new OrderItemsRequest(
                    laptopBagVariantId,
                    1,
                    List.of()
            );

            OrderItemsRequest chairOrder = new OrderItemsRequest(
                    chairVariantId,
                    1,
                    List.of()
            );

            CreateOrderRequest order1 = new CreateOrderRequest(customer1Id, List.of(laptopOrder));
            CreateOrderRequest order2 = new CreateOrderRequest(customer2Id, List.of(smartphoneOrder));
            CreateOrderRequest order3 = new CreateOrderRequest(customer3Id, List.of(monitorOrder));
            CreateOrderRequest order4 = new CreateOrderRequest(customer4Id, List.of(laptopBagOrder));
            CreateOrderRequest order5 = new CreateOrderRequest(customer5Id, List.of(chairOrder));

            List<CreateOrderRequest> orders = List.of(order1, order2, order3, order4, order5);

            for (CreateOrderRequest order : orders) {
                orderService.createOrder(order);
            }


            System.out.println("Sample order created dynamically using Feign!");
        }
    }
}
