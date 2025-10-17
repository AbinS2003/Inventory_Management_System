package com.example.product_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/products")
@Tag(
        name = "product controller",
        description = "Handles CRUD operations for products"
)
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(
            summary = "Add a new product",
            description = "Creates a new product by saving the provided product details into the database."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product data provided"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to save product")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(@RequestBody @Valid ProductRequest productRequest){

        return productService.addProduct(productRequest);
    }


    @PutMapping("{id}")
    @Operation(
            summary = "Update an existing product",
            description = "Fetch a product from database using id and save the updated product in database"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product data provided"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found with the given id"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to save product")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@RequestBody @Valid ProductRequest productRequest,
																	  @Parameter(description = "Unique ID of the product to update") @PathVariable String id){

        return productService.updateProduct(productRequest, id);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete a product",
            description = "Deletes the product with the specified ID from the database."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to delete product")
    })
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@Parameter(description = "Unique ID of the product to delete") @PathVariable String id){

        return productService.deleteProduct(id);
    }


    @GetMapping("{id}")
    @Operation(
            summary = "Fetch a product by id",
            description = "Fetches the product with the specified ID from the database."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to fetch product")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@Parameter(description = "Unique ID of the product to fetch")@PathVariable String id){

        return productService.getProductById(id);
    }

    @GetMapping
    @Operation(
            summary = "Get all products with pagination and sorting",
            description = "Fetches all sorted products from the database in pages."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Products not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to fetch products")
    })
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                                             @RequestParam(defaultValue = "name") String sortBy,
                                                                             @RequestParam(defaultValue = "asc") String sortDir){

        return productService.getAllProducts(page, pageSize, sortBy, sortDir);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Find products by matching keyword",
            description = "Fetches all products that matches the name partially or completely with keyword from the database in pages."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Products not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to fetch products")
    })
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProduct(@RequestParam String keyword,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int pageSize){

        return productService.searchProduct(keyword, page, pageSize);
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filter products by category",
            description = "Fetches all products that matches the category from the database in pages."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Products not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to fetch products")
    })
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> filterProducts(@RequestParam String category,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int pageSize){

        return productService.filterProducts(category, page, pageSize);
    }

    @PutMapping("/reduce/{id}")
    @Operation(
            summary = "Reduce product stock ",
            description = "Reduces the quantity of a specific product by the given amount."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock reduced successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error - Failed to fetch products"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Insufficent stock")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> updateQuantity(@Parameter(description = "Unique ID of the product to update")@PathVariable String id,
                                                                       @RequestParam @Min(value = 1, message = "Quantity must be at least 1")
                                                                       int quantity){

        return productService.updateQuantity(id, quantity);
    }




}
