package com.socialhub.service;

import com.socialhub.dto.*;
import com.socialhub.mapper.ProductMapper;
import com.socialhub.model.Product;
import com.socialhub.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Get all products (basic info)
    public List<ProductBasicDTO> getAllProductsBasic() {
        List<Product> products = productRepository.findAll();
        return productMapper.toProductBasicDTOs(products);
    }

    // Get product by ID (detailed info)
    public ProductDetailDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return productMapper.toProductDetailDTO(product);
    }

    // Create a new product
    public ProductDetailDTO createProduct(ProductDetailDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductDetailDTO(savedProduct);
    }

    // Update a product
    public ProductDetailDTO updateProduct(Long id, ProductDetailDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductDetailDTO(updatedProduct);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    // Add your custom service methods here
}