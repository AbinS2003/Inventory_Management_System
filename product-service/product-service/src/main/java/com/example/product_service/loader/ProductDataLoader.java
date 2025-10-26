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

        if (productRepository.count() == 0) {

            List<ProductRequest> products = List.of(
                    new ProductRequest("Gaming Laptop", "Electronics", List.of(
                            new VariantRequest(75000.0, 10, Map.of("brand", "ASUS", "ram", "16GB", "storage", "512GB SSD")),
                            new VariantRequest(95000.0, 6, Map.of("brand", "ASUS", "ram", "32GB", "storage", "1TB SSD"))
                    )),
                    new ProductRequest("Wireless Mouse", "Electronics", List.of(
                            new VariantRequest(1200.0, 30, Map.of("brand", "Logitech", "type", "Bluetooth"))
                    )),
                    new ProductRequest("Mechanical Keyboard", "Electronics", List.of(
                            new VariantRequest(3500.0, 20, Map.of("brand", "Redragon", "switch", "Blue"))
                    )),
                    new ProductRequest("Smartphone", "Electronics", List.of(
                            new VariantRequest(25000.0, 10, Map.of("brand", "Samsung", "color", "Black", "storage", "128GB")),
                            new VariantRequest(30000.0, 7, Map.of("brand", "Samsung", "color", "Blue", "storage", "256GB"))
                    )),
                    new ProductRequest("Phone Case", "Accessories", List.of(
                            new VariantRequest(500.0, 50, Map.of("color", "Black", "material", "Silicone"))
                    )),
                    new ProductRequest("Earphones", "Electronics", List.of(
                            new VariantRequest(1000.0, 40, Map.of("brand", "Boat", "type", "Wired")),
                            new VariantRequest(2000.0, 25, Map.of("brand", "Sony", "type", "Wireless"))
                    )),
                    new ProductRequest("Smartwatch", "Electronics", List.of(
                            new VariantRequest(8000.0, 12, Map.of("brand", "Noise", "color", "Black")),
                            new VariantRequest(9500.0, 10, Map.of("brand", "Amazfit", "color", "Gray"))
                    )),
                    new ProductRequest("Laptop Bag", "Accessories", List.of(
                            new VariantRequest(1500.0, 20, Map.of("color", "Gray", "capacity", "25L"))
                    )),
                    new ProductRequest("Gaming Mouse Pad", "Accessories", List.of(
                            new VariantRequest(700.0, 40, Map.of("color", "Black", "size", "XL"))
                    )),
                    new ProductRequest("Bluetooth Speaker", "Electronics", List.of(
                            new VariantRequest(2500.0, 15, Map.of("brand", "JBL", "color", "Red"))
                    )),
                    new ProductRequest("LED Monitor", "Electronics", List.of(
                            new VariantRequest(12000.0, 10, Map.of("brand", "Dell", "size", "24 inch"))
                    )),
                    new ProductRequest("Desk Lamp", "Furniture", List.of(
                            new VariantRequest(2000.0, 18, Map.of("color", "White", "material", "Metal"))
                    )),
                    new ProductRequest("Office Chair", "Furniture", List.of(
                            new VariantRequest(6000.0, 12, Map.of("material", "Leather", "color", "Black"))
                    )),
                    new ProductRequest("Coffee Table", "Furniture", List.of(
                            new VariantRequest(8000.0, 10, Map.of("material", "Wood", "color", "Brown"))
                    )),
                    new ProductRequest("Backpack", "Accessories", List.of(
                            new VariantRequest(1800.0, 25, Map.of("color", "Blue", "capacity", "30L"))
                    )),
                    new ProductRequest("Power Bank", "Electronics", List.of(
                            new VariantRequest(2000.0, 20, Map.of("brand", "Mi", "capacity", "10000mAh"))
                    )),
                    new ProductRequest("Webcam", "Electronics", List.of(
                            new VariantRequest(3000.0, 15, Map.of("brand", "Logitech", "resolution", "1080p"))
                    )),
                    new ProductRequest("External Hard Drive", "Electronics", List.of(
                            new VariantRequest(5000.0, 10, Map.of("brand", "Seagate", "capacity", "1TB"))
                    )),
                    new ProductRequest("Router", "Electronics", List.of(
                            new VariantRequest(2500.0, 20, Map.of("brand", "TP-Link", "speed", "1200Mbps"))
                    )),
                    new ProductRequest("USB Cable", "Accessories", List.of(
                            new VariantRequest(300.0, 60, Map.of("type", "Type-C", "length", "1m"))
                    ))
            );

            products.forEach(productService::addProduct);
            System.out.println(" Base products added!");

            var laptop = productRepository.findByNameIgnoreCase("Gaming Laptop").orElseThrow();
            var mouse = productRepository.findByNameIgnoreCase("Wireless Mouse").orElseThrow();
            var keyboard = productRepository.findByNameIgnoreCase("Mechanical Keyboard").orElseThrow();

            laptop.setAddonProductIds(List.of(mouse.getId(), keyboard.getId()));
            productRepository.save(laptop);

            var phone = productRepository.findByNameIgnoreCase("Smartphone").orElseThrow();
            var caseP = productRepository.findByNameIgnoreCase("Phone Case").orElseThrow();
            var earphones = productRepository.findByNameIgnoreCase("Earphones").orElseThrow();
            var powerBank = productRepository.findByNameIgnoreCase("Power Bank").orElseThrow();

            phone.setAddonProductIds(List.of(caseP.getId(), earphones.getId(), powerBank.getId()));
            productRepository.save(phone);

            var monitor = productRepository.findByNameIgnoreCase("LED Monitor").orElseThrow();
            var webcam = productRepository.findByNameIgnoreCase("Webcam").orElseThrow();

            monitor.setAddonProductIds(List.of(webcam.getId()));
            productRepository.save(monitor);

            var deskLamp = productRepository.findByNameIgnoreCase("Desk Lamp").orElseThrow();
            var chair = productRepository.findByNameIgnoreCase("Office Chair").orElseThrow();

            chair.setAddonProductIds(List.of(deskLamp.getId()));
            productRepository.save(chair);

            System.out.println(" Addons linked successfully!");
        }
    }
}
