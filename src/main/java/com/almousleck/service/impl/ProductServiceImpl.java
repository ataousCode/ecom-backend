package com.almousleck.service.impl;

import com.almousleck.dto.ImageDto;
import com.almousleck.dto.ProductDto;
import com.almousleck.exceptions.ProductNotFoundException;
import com.almousleck.entites.Category;
import com.almousleck.entites.Image;
import com.almousleck.entites.Product;
import com.almousleck.repository.CategoryRepository;
import com.almousleck.repository.ImageRepository;
import com.almousleck.repository.ProductRepository;
import com.almousleck.request.AddProductRequest;
import com.almousleck.request.ProductUpdateRequest;
import com.almousleck.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category is found. if yes then set the cat
        Category category = Optional.ofNullable(categoryRepository
                .findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found.")
        );
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository
                .findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {
                    throw new ProductNotFoundException("Product not found.");
                });
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found.")) ;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository
                .findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository
                .findByBrandAndName(name, brand);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository
                .countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream()
                .map(this::convertProductToProductDto).toList();
    }

    @Override
    public ProductDto convertProductToProductDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDto = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDto);
        return productDto;
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                        request.getName(),
                        request.getBrand(),
                        request.getPrice(),
                request.getQuantity(),
                request.getDescription(),
                        category
                );
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
         existingProduct.setName(request.getName());
         existingProduct.setBrand(request.getBrand());
         existingProduct.setPrice(request.getPrice());
         existingProduct.setQuantity(request.getQuantity());
         existingProduct.setDescription(request.getDescription());

         Category category = categoryRepository.findByName(request.getCategory().getName());
         existingProduct.setCategory(category);
         return existingProduct;
    }
}
