package com.example.product_service.loader;

import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductDataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args){

        if (productRepository.count() == 0){
            List<Product> products = List.of(
                    new Product("Gaming Laptop", 75000, 15, "Electronics"),
                    new Product("Smartphone", 25000, 10, "Electronics"),
                    new Product("Wireless Headphones", 3000, 25, "Electronics"),
                    new Product("Bluetooth Speaker", 1500, 30, "Electronics"),
                    new Product("Office Chair", 4500, 15, "Furniture"),
                    new Product("Coffee Table", 6000, 18, "Furniture"),
                    new Product("Dining Table", 12000, 15, "Furniture"),
                    new Product("LED Monitor", 12000, 17, "Electronics"),
                    new Product("Mechanical Keyboard", 3500, 20, "Electronics"),
                    new Product("Mouse Pad", 500, 50, "Electronics"),
                    new Product("Bookshelf", 7000, 10, "Furniture"),
                    new Product("Floor Lamp", 2000, 12, "Furniture"),
                    new Product("Backpack", 1800, 20, "Accessories"),
                    new Product("Wrist Watch", 8000, 10, "Accessories"),
                    new Product("Sunglasses", 1500, 15, "Accessories"),
                    new Product("Yoga Mat", 1200, 25, "Sports"),
                    new Product("Treadmill", 45000, 13, "Sports"),
                    new Product("Basketball", 800, 30, "Sports"),
                    new Product("LED TV", 40000, 14, "Electronics"),
                    new Product("Smart Home Speaker", 3500, 15, "Electronics")
            );

            productRepository.saveAll(products);

            System.out.println("sample products loaded!");

        }


    }
}
