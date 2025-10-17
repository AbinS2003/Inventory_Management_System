package com.example.order_service.repository;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.CategorySalesReportDTO;
import com.example.order_service.dto.ProductSalesReportDTO;
import com.example.order_service.model.Order;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {


    @Aggregation(pipeline = {
            "{ $unwind: '$items' }",
            "{ $group: { " +
                    "_id: '$items.productId', " +
                    "productName: { $first: '$items.productName' }, " +
                    "category: { $first: '$items.category' }, " +
                    "totalQuantity: { $sum: '$items.qty' }, " +
                    "totalAmount: { $sum: '$items.totalPrice' } } }",
            "{ $project: { " +
                    "_id: 0, " +
                    "productId: '$_id', " +
                    "productName: 1, " +
                    "category: 1, " +
                    "totalQuantity: 1, " +
                    "totalAmount: 1 } }"
    })
    List<ProductSalesReportDTO> getAllProductSales();



    @Aggregation(pipeline = {
            "{$unwind: '$items'}",
            "{ $group: {" +
                    "_id: '$items.category'," +
                    "categoryName: { $first: '$items.category' }," +
                    "totalQuantity: { $sum : '$items.qty' }," +
                    "totalAmount: { $sum : '$items.totalPrice' } } }",
            "{ $project: { " +
                    "_id: 0, " +
                    "categoryName: '$_id', " +
                    "totalQuantity: 1, " +
                    "totalAmount: 1 " +
                    "} }"
    })
    List<CategorySalesReportDTO> getAllCategorySales();
}
