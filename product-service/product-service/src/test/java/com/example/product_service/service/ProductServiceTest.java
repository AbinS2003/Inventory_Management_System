package com.example.product_service.service;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct() {

        var request = new ProductRequest("Phone", 20000.0, 5, "Electronics");

        var savedProduct = new Product("1", "Phone", 20000.0, 5, "Electronics");

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        var response = productService.addProduct(request);

        assertEquals(201, response.getBody().getStatus());
        assertEquals("Product Saved successfully", response.getBody().getMessage());
        assertEquals("Phone", response.getBody().getData().getName());


    }

    @Test
    void updateProduct() {

        String id = "123";

        var existingProduct = new Product("Old Name", 100.5, 5, "Electronics");
        existingProduct.setId(id);
        var productRequest = new ProductRequest("New Name", 120.5, 10, "Electronics");

        var updatedProduct = new Product("New Name", 120.5, 10, "Electronics");
        updatedProduct.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        var response = productService.updateProduct(productRequest, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product updated successfully", response.getBody().getMessage());
        assertEquals("New Name", response.getBody().getData().getName());
        assertEquals(120.5, response.getBody().getData().getPrice());
        assertEquals(10, response.getBody().getData().getQuantity());
    }

    @Test
    void deleteProduct() {

        String id = "123";

        when(productRepository.existsById(id)).thenReturn(true);

        var response = productService.deleteProduct(id);

        assertEquals("Product deleted successfully", response.getBody().getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getData());

    }

    @Test
    void getProductById() {

        String id = "123";

        var existingProduct = new Product("Phone", 20000.5, 5, "Electronics");
        existingProduct.setId(id);

        when(productRepository.existsById(id)).thenReturn(true);
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));

        var response = productService.getProductById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product fetched", response.getBody().getMessage());
        assertEquals("Phone", response.getBody().getData().getName());

    }

    @Test
    void getAllProducts() {

        int page = 0;
        int pageSize = 2;
        String sortBy = "name";
        String sortDir = "asc";

        var product1 = new Product("123","Phone", 20000.5, 5, "Electronics");
        var product2 = new Product("234","Laptop", 400000.0, 5, "Electronics");
        var product3 = new Product("345","Shoes", 2000.0, 5, "Footwear");
        var product4 = new Product("456","T shirt", 400.0, 5, "Clothing");

        List<Product> products = List.of(product1,product2,product3,product4);
        Page<Product> productPage = new PageImpl<>(products);

        Pageable pageable = PageRequest.of(page, pageSize,
                sortDir.equalsIgnoreCase("asc") ?
                        Sort.by(sortBy).ascending() :
                        Sort.by(sortBy).descending());

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        var response = productService.getAllProducts(page, pageSize, sortBy, sortDir);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Products fetched", response.getBody().getMessage());
        assertEquals(4, response.getBody().getData().getContent().size());
        assertEquals("Phone", response.getBody().getData().getContent().get(0).getName());
    }

    @Test
    void searchProduct() {

        String keyword = "phone";
        int page = 0;
        int pageSize = 2;

        var product1 = new Product("123","Phone", 20000.5, 5, "Electronics");
        var product5 = new Product("567","Smart Phone", 30000.5, 5, "Electronics");

        Pageable pageable = PageRequest.of(page, pageSize);
        List<Product> products = List.of(product1,product5);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.findByNameContainingIgnoreCase(keyword, pageable)).thenReturn(productPage);

        var response = productService.searchProduct(keyword, page, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Products found", response.getBody().getMessage());
        assertEquals("Smart Phone", response.getBody().getData().getContent().get(1).getName());

    }

    @Test
    void filterProducts() {

        String category = "Electronics";
        int page = 0;
        int pageSize = 2;

        var product1 = new Product("123","Phone", 20000.5, 5, "Electronics");
        var product5 = new Product("567","Smart Phone", 30000.5, 5, "Electronics");

        Pageable pageable = PageRequest.of(page, pageSize);
        List<Product> products = List.of(product1,product5);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.findByCategory(category, pageable)).thenReturn(productPage);

        var response = productService.filterProducts(category, page, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Products found", response.getBody().getMessage());
        assertEquals("Smart Phone", response.getBody().getData().getContent().get(1).getName());

    }

}