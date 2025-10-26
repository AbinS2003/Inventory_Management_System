package com.example.product_service.service;

import com.example.product_service.dto.*;
import com.example.product_service.exception.ErrorMessage;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.exception.ProductsNotFoundException;
import com.example.product_service.exception.VariantNotFoundException;
import com.example.product_service.model.Product;
import com.example.product_service.model.Variant;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.repository.VariantRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;

    public ProductService(ProductRepository productRepository, VariantRepository variantRepository) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
    }

    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(ProductRequest productRequest) {
        try {

            if (productRequest.getAddonProductIds() != null && !productRequest.getAddonProductIds().isEmpty()) {

                List<String> invalidAddonIds = new ArrayList<>();

                for (String addonId : productRequest.getAddonProductIds()) {
                    if (!productRepository.existsById(addonId)) {
                        invalidAddonIds.add(addonId);
                    }
                }

                if (!invalidAddonIds.isEmpty()) {
                    throw new ProductNotFoundException(ErrorMessage.ADDON_PRODUCT_NOT_FOUND, invalidAddonIds.toString());
                }
            }

            Product product = new Product(productRequest.getName(), productRequest.getCategory());
            product.setAddonProductIds(productRequest.getAddonProductIds());

            Product savedProduct = productRepository.save(product);

            List<VariantResponse> variantResponses = new ArrayList<>();
            for (VariantRequest vReq : productRequest.getVariants()) {
                Variant variant = new Variant();
                variant.setProductId(savedProduct.getId());
                variant.setAttributes(vReq.getAttributes());
                variant.setPrice(vReq.getPrice());
                variant.setQuantity(vReq.getQuantity());

                Variant savedVariant = variantRepository.save(variant);

                variantResponses.add(new VariantResponse(
                        savedVariant.getId(),
                        savedVariant.getProductId(),
                        savedVariant.getAttributes(),
                        savedVariant.getPrice(),
                        savedVariant.getQuantity()
                ));
            }

            ProductResponse productResponse = new ProductResponse(
                    savedProduct.getId(),
                    savedProduct.getName(),
                    savedProduct.getCategory(),
                    variantResponses,
                    null
            );

            if (savedProduct.getAddonProductIds() != null && !savedProduct.getAddonProductIds().isEmpty()) {
                List<Product> addonProducts = productRepository.findAllById(savedProduct.getAddonProductIds());
                List<AddonResponse> addonResponses = addonProducts.stream()
                        .map(addon -> {
                            List<Variant> addonVariants = variantRepository.findByProductId(addon.getId());
                            List<VariantResponse> addonVariantResponses = addonVariants.stream()
                                    .map(v -> new VariantResponse(
                                            v.getId(),
                                            v.getProductId(),
                                            v.getAttributes(),
                                            v.getPrice(),
                                            v.getQuantity()
                                    )).toList();
                            return new AddonResponse(
                                    addon.getId(),
                                    addon.getName(),
                                    addon.getCategory(),
                                    addonVariantResponses
                            );
                        }).toList();
                productResponse.setAddonProducts(addonResponses);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Product Saved successfully",
                    productResponse
            ));

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to save product: " + e.getMessage(),
                            null)
            );
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(ProductRequest productRequest, String id) {

        Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

        if (productRequest.getName() != null) {
            product.setName(productRequest.getName());
        }
        if (productRequest.getCategory() != null) {
            product.setCategory(productRequest.getCategory());
        }

        if (productRequest.getAddonProductIds() != null) {
            product.setAddonProductIds(productRequest.getAddonProductIds());
        }

        if(productRequest.getAddonProductIds() != null && !productRequest.getAddonProductIds().isEmpty()){

            Set<String> updatedAddOns = new HashSet<>();
            if (product.getAddonProductIds() != null){
                updatedAddOns.addAll(product.getAddonProductIds());
            }
            updatedAddOns.addAll(productRequest.getAddonProductIds());
            product.setAddonProductIds(new ArrayList<>(updatedAddOns));
        }

        Product updatedProduct = productRepository.save(product);

        if (productRequest.getVariants() != null){

            for(VariantRequest vReq : productRequest.getVariants()){

                Variant variant;

                if(vReq.getId() != null){
                    variant = variantRepository.findById(vReq.getId())
                                    .orElseThrow(() -> new VariantNotFoundException(vReq.getId()));

                    if (!variant.getProductId().equals(updatedProduct.getId())) {
                        throw new IllegalStateException(ErrorMessage.VARIANT_DOES_NOT_BELONG_TO_PRODUCT.getMessage());
                    }

                    variant.setPrice(vReq.getPrice());
                    variant.setQuantity(vReq.getQuantity());
                    variant.setAttributes(vReq.getAttributes());
                }else {
                    variant = new Variant();
                    variant.setProductId(updatedProduct.getId());
                    variant.setPrice(vReq.getPrice());
                    variant.setQuantity(vReq.getQuantity());
                    variant.setAttributes(vReq.getAttributes());
                }
                Variant savedVariant = variantRepository.save(variant);
            }
        }

        List<Variant> allVariants = variantRepository.findByProductId(updatedProduct.getId());
        List<VariantResponse> variantResponses = allVariants.stream()
                .map(v -> new VariantResponse(
                        v.getId(),
                        v.getProductId(),
                        v.getAttributes(),
                        v.getPrice(),
                        v.getQuantity()
                )).toList();

            ProductResponse productResponse = new ProductResponse(
                    updatedProduct.getId(),
                    updatedProduct.getName(),
                    updatedProduct.getCategory(),
                    variantResponses,
                    null
            );

            if(updatedProduct.getAddonProductIds() != null && !updatedProduct.getAddonProductIds().isEmpty()){

                List<Product> addOnProducts = productRepository.findAllById(updatedProduct.getAddonProductIds());
                List<AddonResponse> addOnResponses = addOnProducts.stream()
                        .map(addOn -> {
                            List<Variant> addOnVariants = variantRepository.findByProductId(addOn.getId());
                            List<VariantResponse> addOnVariantResponses = addOnVariants.stream()
                                    .map(v -> new VariantResponse(
                                            v.getId(),
                                            v.getProductId(),
                                            v.getAttributes(),
                                            v.getPrice(),
                                            v.getQuantity()
                                    )).toList();

                            return new AddonResponse(
                                    addOn.getId(),
                                    addOn.getName(),
                                    addOn.getCategory(),
                                    addOnVariantResponses
                            );
                        }).toList();

                productResponse.setAddonProducts(addOnResponses);
            }

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

            variantRepository.deleteByProductId(id);
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

        List<Variant> variants = variantRepository.findByProductId(id);

        List<VariantResponse> variantResponses = variants.stream()
                .map(v -> new VariantResponse(
                        v.getId(),
                        v.getProductId(),
                        v.getAttributes(),
                        v.getPrice(),
                        v.getQuantity()

                )).toList();


        ProductResponse productResponse = new ProductResponse(product.getId(),
                                                                product.getName(),
                                                                product.getCategory(),
                                                                variantResponses,
                                                                null);

        if (product.getAddonProductIds() != null && !product.getAddonProductIds().isEmpty()){

            List<Product> addonProducts = productRepository.findAllById(product.getAddonProductIds());
            List<AddonResponse> addonResponses = addonProducts.stream()
                    .map(addon -> {
                        List<Variant> addonVariants = variantRepository.findByProductId(addon.getId());
                        List<VariantResponse> addonVariantResponses = addonVariants.stream()
                                .map(v -> new VariantResponse(
                                        v.getId(),
                                        v.getProductId(),
                                        v.getAttributes(),
                                        v.getPrice(),
                                        v.getQuantity()
                                )).toList();

                        return new AddonResponse(
                                addon.getId(),
                                addon.getName(),
                                addon.getCategory(),
                                addonVariantResponses
                        );
                    }).toList();

            productResponse.setAddonProducts(addonResponses);
        }

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

        List<String> productIds = productPage.getContent()
                                                .stream()
                                                .map(Product :: getId)
                                                .toList();

        List<Variant> allVariants = variantRepository.findByProductIdIn(productIds);

        Map<String, List<Variant>> variantMap = allVariants.stream()
                .collect(Collectors.groupingBy(Variant :: getProductId));

        List<String> allAddonIds = productPage.getContent().stream()
                .filter(p -> p.getAddonProductIds() != null)
                .flatMap(p -> p.getAddonProductIds().stream())
                .distinct()
                .toList();

        Map<String, Product> addonProductMap = allAddonIds.isEmpty() ? Map.of() :
                productRepository.findAllById(allAddonIds).stream()
                        .collect(Collectors.toMap(Product::getId, p -> p));

        List<Variant> allAddonVariants = allAddonIds.isEmpty() ? List.of() :
                variantRepository.findByProductIdIn(allAddonIds);

        Map<String, List<Variant>> addonVariantMap = allAddonVariants.stream()
                .collect(Collectors.groupingBy(Variant::getProductId));

        Page<ProductResponse> productResponsePage = productPage.map(p -> {

            List<VariantResponse> variantResponses = variantMap.getOrDefault(p.getId(), List.of())
                    .stream()
                    .map(v -> new VariantResponse(
                            v.getId(),
                            v.getProductId(),
                            v.getAttributes(),
                            v.getPrice(),
                            v.getQuantity()
                    )).toList();

            List<AddonResponse> addonResponses = p.getAddonProductIds() == null ? List.of() :
                    p.getAddonProductIds().stream()
                            .map(addonId -> {
                                Product addon = addonProductMap.get(addonId);
                                if (addon == null) return null;

                                List<VariantResponse> addonVariantResponses = addonVariantMap
                                        .getOrDefault(addonId, List.of())
                                        .stream()
                                        .map(v -> new VariantResponse(
                                                v.getId(),
                                                v.getProductId(),
                                                v.getAttributes(),
                                                v.getPrice(),
                                                v.getQuantity()
                                        )).toList();

                                return new AddonResponse(
                                        addon.getId(),
                                        addon.getName(),
                                        addon.getCategory(),
                                        addonVariantResponses
                                );
                            })
                            .filter(Objects::nonNull)
                            .toList();

            return new ProductResponse(
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    variantResponses,
                    addonResponses
            );
        });

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

        List<String> productIds = productsPage.getContent()
                .stream()
                .map(Product :: getId)
                .toList();

        List<Variant> allVariants = variantRepository.findByProductIdIn(productIds);

        Map<String, List<Variant>> variantMap = allVariants.stream()
                .collect(Collectors.groupingBy(Variant :: getProductId));

        List<String> allAddonIds = productsPage.getContent().stream()
                .filter(p -> p.getAddonProductIds() != null)
                .flatMap(p -> p.getAddonProductIds().stream())
                .toList();

        Map<String, Product> addonProductMap = allAddonIds.isEmpty() ? Map.of() :
                productRepository.findAllById(allAddonIds).stream()
                        .collect(Collectors.toMap(Product::getId, p -> p));

        List<Variant> allAddonVariants = allAddonIds.isEmpty() ? List.of() :
                variantRepository.findByProductIdIn(allAddonIds);

        Map<String, List<Variant>> addonVariantMap = allAddonVariants.stream()
                .collect(Collectors.groupingBy(Variant::getProductId));


        Page<ProductResponse> productResponsePage = productsPage.map(p -> {

            List<VariantResponse> variantResponses = variantMap.getOrDefault(p.getId(), List.of())
                    .stream()
                    .map(v -> new VariantResponse(
                            v.getId(),
                            v.getProductId(),
                            v.getAttributes(),
                            v.getPrice(),
                            v.getQuantity()
                    )).toList();

            List<AddonResponse> addonResponses = p.getAddonProductIds() == null ? List.of() :
                    p.getAddonProductIds().stream()
                            .map(addonId -> {
                                Product addon = addonProductMap.get(addonId);
                                if (addon == null) return null;

                                List<VariantResponse> addonVariantResponses = addonVariantMap
                                        .getOrDefault(addonId, List.of())
                                        .stream()
                                        .map(v -> new VariantResponse(
                                                v.getId(),
                                                v.getProductId(),
                                                v.getAttributes(),
                                                v.getPrice(),
                                                v.getQuantity()
                                        )).toList();

                                return new AddonResponse(
                                        addon.getId(),
                                        addon.getName(),
                                        addon.getCategory(),
                                        addonVariantResponses
                                );
                            })
                            .filter(Objects::nonNull)
                            .toList();

            return new ProductResponse(
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    variantResponses,
                    addonResponses
            );
        });

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Products found",
                productResponsePage
        ));
    }

    public ResponseEntity<ApiResponse<Page<ProductResponse>>> filterProducts(String category, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Product> productsPage = productRepository.findByCategory(category, pageable);

        if (productsPage.isEmpty()){
            throw new ProductsNotFoundException(ErrorMessage.NO_RESULTS_FOUND);
        }


        List<String> productIds = productsPage.getContent()
                .stream()
                .map(Product :: getId)
                .toList();

        List<Variant> allVariants = variantRepository.findByProductIdIn(productIds);

        Map<String, List<Variant>> variantMap = allVariants.stream()
                .collect(Collectors.groupingBy(Variant :: getProductId));


        List<String> allAddonIds = productsPage.getContent().stream()
                .filter(p -> p.getAddonProductIds() != null)
                .flatMap(p -> p.getAddonProductIds().stream())
                .toList();

        Map<String, Product> addonProductMap = allAddonIds.isEmpty() ? Map.of() :
                productRepository.findAllById(allAddonIds).stream()
                        .collect(Collectors.toMap(Product::getId, p -> p));

        List<Variant> allAddonVariants = allAddonIds.isEmpty() ? List.of() :
                variantRepository.findByProductIdIn(allAddonIds);

        Map<String, List<Variant>> addonVariantMap = allAddonVariants.stream()
                .collect(Collectors.groupingBy(Variant::getProductId));


        Page<ProductResponse> productResponsePage = productsPage.map(p -> {

            List<VariantResponse> variantResponses = variantMap.getOrDefault(p.getId(), List.of())
                    .stream()
                    .map(v -> new VariantResponse(
                            v.getId(),
                            v.getProductId(),
                            v.getAttributes(),
                            v.getPrice(),
                            v.getQuantity()
                    )).toList();

            List<AddonResponse> addonResponses = p.getAddonProductIds() == null ? List.of() :
                    p.getAddonProductIds().stream()
                            .map(addonId -> {
                                Product addon = addonProductMap.get(addonId);
                                if (addon == null) return null;

                                List<VariantResponse> addonVariantResponses = addonVariantMap
                                        .getOrDefault(addonId, List.of())
                                        .stream()
                                        .map(v -> new VariantResponse(
                                                v.getId(),
                                                v.getProductId(),
                                                v.getAttributes(),
                                                v.getPrice(),
                                                v.getQuantity()
                                        )).toList();

                                return new AddonResponse(
                                        addon.getId(),
                                        addon.getName(),
                                        addon.getCategory(),
                                        addonVariantResponses
                                );
                            })
                            .filter(Objects::nonNull)
                            .toList();

            return new ProductResponse(
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    variantResponses,
                    addonResponses
            );
        });

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Products found",
                productResponsePage
        ));
    }


    //for sample order data loading
    public List<String> getVariantIdsByProductName(String name) {

        name = name.trim();
        String finalName = name;
        Product product = productRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Product not found: " + finalName));
        return variantRepository.findByProductId(product.getId())
                .stream()
                .map(Variant::getId)
                .toList();

    }
}
