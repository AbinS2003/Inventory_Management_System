package com.example.order_service.service;

import com.example.order_service.exception.*;
import com.example.order_service.dto.*;
import com.example.order_service.feign.ProductClient;
import com.example.order_service.feign.VariantClient;
import com.example.order_service.model.AddonItem;
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

            double mainTotalPrice = variant.getPrice() * orderItemsRequest.getQuantity();
            double addonPrice = 0;

            List<AddonItem> addonItems = new ArrayList<>();
            if (orderItemsRequest.getAddons() != null){

                for (AddonRequest addonReq : orderItemsRequest.getAddons()){

                    if (addonReq.getVariantId() == null || addonReq.getVariantId().isBlank()) continue;

                    VariantResponseDTO addonVariant;
                    try{
                        ApiResponse<VariantResponseDTO> addonVariantResp =  variantClient.getVariantById(addonReq.getVariantId());
                        addonVariant = addonVariantResp.getData();

                        if (addonVariant == null) {
                            throw new VariantNotFoundException(addonReq.getVariantId());
                        }
                    } catch (FeignException.NotFound e) {
                        throw new VariantNotFoundException(addonReq.getVariantId());
                    }

                    ProductResponseDTO addonProduct;

                    try{
                        ApiResponse<ProductResponseDTO> addonProductResp = productClient.getProductById(addonVariant.getProductId());
                        addonProduct = addonProductResp.getData();

                        if (addonProduct == null) {
                            throw new ProductNotFoundException(addonVariant.getProductId());
                        }

                    } catch (FeignException.NotFound e){
                        throw new ProductNotFoundException(addonVariant.getProductId());
                    }

                    if (addonVariant.getQuantity() < 1) {
                        throw new InsufficentStockException(addonVariant.getVariantId());
                    }

                    variantClient.updateStock(addonVariant.getVariantId(), 1);

                    AddonItem addonItem = new AddonItem();

                    addonItem.setAddonProductId(addonProduct.getId());
                    addonItem.setAddonVariantId(addonVariant.getVariantId());
                    addonItem.setAddonName(addonProduct.getName());
                    addonItem.setAttributes(addonVariant.getAttributes());
                    addonItem.setPrice(addonVariant.getPrice());

                    addonItems.add(addonItem);

                    addonPrice += addonItem.getPrice();
                }
            }

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    variant.getVariantId(),
                    product.getName(),
                    product.getCategory(),
                    variant.getAttributes(),
                    orderItemsRequest.getQuantity(),
                    variant.getPrice(),
                    mainTotalPrice,
                    addonItems
            );

            orderItems.add(orderItem);
            totalAmount += mainTotalPrice + addonPrice;
        }

        Order order = new Order(createOrderRequest.getCustomerId(),
                                customer.getName(),
                                orderItems,
                                totalAmount);

        Order savedOrder = orderRepository.save(order);

        List<OrderItemResponseDTO> orderItemResponseDTOs = savedOrder.getItems().stream()
                                                            .map(item -> {

                                                                List<AddonItemsResponseDTO> addonResponses = item.getAddonItems().stream()
                                                                        .map(addon -> {
                                                                            AddonItemsResponseDTO dto = new AddonItemsResponseDTO();
                                                                            dto.setAddonProductId(addon.getAddonProductId());
                                                                            dto.setAddonVariantId(addon.getAddonVariantId());
                                                                            dto.setAddonName(addon.getAddonName());
                                                                            dto.setAttributes(addon.getAttributes());
                                                                            dto.setPrice(addon.getPrice());
                                                                            return dto;
                                                                        }).toList();

                                                                OrderItemResponseDTO dto = new OrderItemResponseDTO();
                                                                dto.setProductId(item.getProductId());
                                                                dto.setVariantId(item.getVariantId());
                                                                dto.setProductName(item.getProductName());
                                                                dto.setCategory(item.getCategory());
                                                                dto.setAttributes(item.getAttributes());
                                                                dto.setQty(item.getQty());
                                                                dto.setPrice(item.getPrice());
                                                                dto.setTotalPrice(item.getTotalPrice());
                                                                dto.setAddons(addonResponses);
                                                                return dto;
                                                                    }).toList();

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

        List<OrderItemResponseDTO> orderItems = order.getItems().stream()
                .map(item -> {
                    List<AddonItemsResponseDTO> addonItemsResponses = item.getAddonItems().stream()
                            .map(addon -> {
                                AddonItemsResponseDTO dto = new AddonItemsResponseDTO();
                                dto.setAddonProductId(addon.getAddonProductId());
                                dto.setAddonVariantId(addon.getAddonVariantId());
                                dto.setAddonName(addon.getAddonName());
                                dto.setAttributes(addon.getAttributes());
                                dto.setPrice(addon.getPrice());
                                return dto;
                            }).toList();

                    OrderItemResponseDTO dto = new OrderItemResponseDTO();
                    dto.setProductId(item.getProductId());
                    dto.setVariantId(item.getVariantId());
                    dto.setProductName(item.getProductName());
                    dto.setCategory(item.getCategory());
                    dto.setAttributes(item.getAttributes());
                    dto.setQty(item.getQty());
                    dto.setPrice(item.getPrice());
                    dto.setTotalPrice(item.getTotalPrice());
                    dto.setAddons(addonItemsResponses);
                    return dto;
                }).toList();


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
                       .map(item ->{

                           List<AddonItemsResponseDTO> addonItemsResponses = item.getAddonItems().stream()
                                   .map(addon -> {
                                       AddonItemsResponseDTO dto = new AddonItemsResponseDTO();
                                       dto.setAddonProductId(addon.getAddonProductId());
                                       dto.setAddonVariantId(addon.getAddonVariantId());
                                       dto.setAddonName(addon.getAddonName());
                                       dto.setAttributes(addon.getAttributes());
                                       dto.setPrice(addon.getPrice());
                                       return dto;
                                   }).toList();

                           OrderItemResponseDTO dto = new OrderItemResponseDTO();
                           dto.setProductId(item.getProductId());
                           dto.setVariantId(item.getVariantId());
                           dto.setProductName(item.getProductName());
                           dto.setCategory(item.getCategory());
                           dto.setAttributes(item.getAttributes());
                           dto.setQty(item.getQty());
                           dto.setPrice(item.getPrice());
                           dto.setTotalPrice(item.getTotalPrice());
                           dto.setAddons(addonItemsResponses);
                           return dto;
                       }).toList();

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
