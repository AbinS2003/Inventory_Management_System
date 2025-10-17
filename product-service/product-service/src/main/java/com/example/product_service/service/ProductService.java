package com.example.product_service.service;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.exception.ErrorMessage;
import com.example.product_service.exception.InsufficentStockException;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.exception.ProductsNotFoundException;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
        public ResponseEntity<ApiResponse<ProductResponse>> addProduct(ProductRequest productRequest) {

        try {
            Product product = new Product(productRequest.getName(),
                    productRequest.getPrice(),
                    productRequest.getQuantity(),
                    productRequest.getCategory());

            Product saved = productRepository.save(product);

            ProductResponse productResponse = new ProductResponse(saved.getId(),
                    saved.getName(),
                    saved.getPrice(),
                    saved.getQuantity(),
                    saved.getCategory());

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Product Saved successfully",
                    productResponse
            ));
        } catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to save product: " + e.getMessage(),
                            null)
            );
        }
    }

    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(ProductRequest productRequest, String id) {

            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

        if (productRequest.getName() != null) {
            product.setName(productRequest.getName());
        }
        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }
        if (productRequest.getQuantity() != null) {
            product.setQuantity(productRequest.getQuantity());
        }
        if (productRequest.getCategory() != null) {
            product.setCategory(productRequest.getCategory());
        }

            Product updated = productRepository.save(product);

            ProductResponse productResponse = new ProductResponse(
                    updated.getId(),
                    updated.getName(),
                    updated.getPrice(),
                    updated.getQuantity(),
                    updated.getCategory()
            );

            return ResponseEntity.ok(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Product updated successfully",
                    productResponse
            ));
    }


    public ResponseEntity<ApiResponse<Void>> deleteProduct(String id) {

            if(! productRepository.existsById(id)){
                throw new ProductNotFoundException(id);
            }
            productRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Product deleted successfully",
                    null
            ));
    }


    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(String id) {

        if(! productRepository.existsById(id)){
            throw new ProductNotFoundException(id);
        }

        Optional<Product> productOpt = productRepository.findById(id);
        Product product = productOpt.get();

        ProductResponse productResponse = new ProductResponse(product.getId(),
                                                                product.getName(),
                                                                product.getPrice(),
                                                                product.getQuantity(),
                                                                product.getCategory());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Product fetched",
                productResponse
        ));
    }

    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(int page, int pageSize, String sortBy, String sortDir) {

        Pageable pageable = PageRequest.of(page, pageSize,
                sortDir.equalsIgnoreCase("asc") ?
                        Sort.by(sortBy).ascending() :
                        Sort.by(sortBy).descending()
        );

        Page<Product> productPage = productRepository.findAll(pageable);

        if(productPage.isEmpty()){
            throw new ProductsNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        Page<ProductResponse> productResponsePage = productPage.map(p ->
                new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getCategory())
        );

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Products fetched",
                productResponsePage
        ));
    }


    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProduct(String keyword, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Product> productsPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);

        if (productsPage.isEmpty()){
            throw new ProductsNotFoundException(ErrorMessage.NO_RESULTS_FOUND);
        }


        Page<ProductResponse> productResponsePage = productsPage.map(p->
            new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getCategory())
        );

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Products found",
                productResponsePage
        ));
    }

    public ResponseEntity<ApiResponse<Page<ProductResponse>>> filterProducts(String category, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Product> productPage = productRepository.findByCategory(category, pageable);

        if (productPage.isEmpty()){
            throw new ProductsNotFoundException(ErrorMessage.NO_RESULTS_FOUND);
        }

        Page<ProductResponse> productResponsePage = productPage.map(p->
                new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getCategory())
        );

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Products found",
                productResponsePage
        ));
    }


    public ResponseEntity<ApiResponse<ProductResponse>> updateQuantity(String id, int quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));


        if (product.getQuantity() < quantity){
            throw new InsufficentStockException(product.getId());
        }

        product.setQuantity(product.getQuantity() - quantity);
        Product saved = productRepository.save(product);

        ProductResponse productResponse = new ProductResponse(saved.getId(),
                                            saved.getName(),
                                            saved.getPrice(),
                                            saved.getQuantity(),
                                            saved.getCategory());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Product quantity updated ",
                productResponse
        ));
    }
}
