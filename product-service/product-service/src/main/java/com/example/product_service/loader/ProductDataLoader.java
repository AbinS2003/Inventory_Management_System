package com.example.product_service.loader;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.VariantRequest;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductDataLoader implements CommandLineRunner {

    private final ProductService productService;
    private final ProductRepository productRepository;

    public ProductDataLoader(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {

        if (productRepository.count()==0) {

            List<ProductRequest> products = List.of(

                    new ProductRequest("Gaming Laptop", "Electronics", List.of(
                            new VariantRequest( 75000.0, 15, Map.of("brand", "ASUS", "ram", "16GB", "storage", "512GB SSD")),
                            new VariantRequest(95000.0, 8, Map.of("brand", "ASUS", "ram", "32GB", "storage", "1TB SSD"))
                    )),

                    new ProductRequest("Smartphone", "Electronics", List.of(
                            new VariantRequest(25000.0, 10, Map.of("brand", "Samsung", "color", "Black", "storage", "128GB")),
                            new VariantRequest(30000.0, 7, Map.of("brand", "Samsung", "color", "Blue", "storage", "256GB"))
                    )),

                    new ProductRequest("Wireless Headphones", "Electronics", List.of(
                            new VariantRequest(3000.0, 25, Map.of("brand", "Sony", "color", "Black"))
                    )),

                    new ProductRequest("Office Chair", "Furniture", List.of(
                            new VariantRequest(4500.0, 15, Map.of("color", "Black", "material", "Leather")),
                            new VariantRequest(4000.0, 20, Map.of("color", "Gray", "material", "Mesh"))
                    )),

                    new ProductRequest("Coffee Table", "Furniture", List.of(
                            new VariantRequest(6000.0, 18, Map.of("material", "Wood", "color", "Brown"))
                    )),

                    new ProductRequest("Dining Table", "Furniture", List.of(
                            new VariantRequest(12000.0, 15, Map.of("material", "Wood", "color", "Oak"))
                    )),

                    new ProductRequest("LED Monitor", "Electronics", List.of(
                            new VariantRequest(12000.0, 17, Map.of("brand", "Dell", "size", "24 inch"))
                    )),

                    new ProductRequest("Mechanical Keyboard", "Electronics", List.of(
                            new VariantRequest(3500.0, 20, Map.of("brand", "Logitech", "switch", "Blue"))
                    )),

                    new ProductRequest("Mouse Pad", "Electronics", List.of(
                            new VariantRequest(500.0, 50, Map.of("color", "Black"))
                    )),

                    new ProductRequest("Bookshelf", "Furniture", List.of(
                            new VariantRequest(7000.0, 10, Map.of("material", "Wood", "shelves", "5"))
                    )),

                    new ProductRequest("Floor Lamp", "Furniture", List.of(
                            new VariantRequest(2000.0, 12, Map.of("material", "Metal", "color", "Silver"))
                    )),

                    new ProductRequest("Backpack", "Accessories", List.of(
                            new VariantRequest(1800.0, 20, Map.of("color", "Black", "capacity", "20L"))
                    )),

                    new ProductRequest("Wrist Watch", "Accessories", List.of(
                            new VariantRequest(8000.0, 10, Map.of("brand", "Fossil", "color", "Brown"))
                    )),

                    new ProductRequest("Sunglasses", "Accessories", List.of(
                            new VariantRequest(1500.0, 15, Map.of("brand", "Ray-Ban", "color", "Black"))
                    )),

                    new ProductRequest("Yoga Mat", "Sports", List.of(
                            new VariantRequest(1200.0, 25, Map.of("color", "Purple", "length", "6ft"))
                    )),

                    new ProductRequest("Treadmill", "Sports", List.of(
                            new VariantRequest( 45000.0, 13, Map.of("brand", "FitPro", "motor", "2.5HP"))
                    )),

                    new ProductRequest("Basketball", "Sports", List.of(
                            new VariantRequest(800.0, 30, Map.of("brand", "Spalding", "size", "7"))
                    )),

                    new ProductRequest("LED TV", "Electronics", List.of(
                            new VariantRequest(40000.0, 14, Map.of("brand", "Samsung", "size", "50 inch"))
                    )),

                    new ProductRequest("Smart Home Speaker", "Electronics", List.of(
                            new VariantRequest(3500.0, 15, Map.of("brand", "Amazon", "model", "Echo Dot"))
                    )),

                    new ProductRequest("Gaming Mouse", "Electronics", List.of(
                            new VariantRequest(2500.0, 18, Map.of("brand", "Razer", "dpi", "16000"))
                    ))
            );

            products.forEach(productService::addProduct);

            System.out.println("sample products with variants loaded via ProductService!");
        }
    }
}
