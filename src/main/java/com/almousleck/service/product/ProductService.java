package com.almousleck.service.product;

import com.almousleck.model.Product;
import com.almousleck.request.AddProductRequest;
import com.almousleck.request.ProductUpdateRequest;

import java.util.List;

public interface ProductService {
    Product addProduct(AddProductRequest request);
    Product getProductById(Long id);
    void deleteProduct(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
}
