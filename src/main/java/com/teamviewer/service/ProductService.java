package com.teamviewer.service;

import com.teamviewer.entity.Product;
import com.teamviewer.exception.ResourceNotFoundException;
import com.teamviewer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<Product> findAllProducts() {
    log.info("findAllProducts invoked ");

    return productRepository.findAll();
  }

  public Product getProductById(Long id) {
    log.info("getProductById invoked " + id.toString());
    return productRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
  }

  public Product createProduct(Product product) {
    log.info("createProduct invoked ");

    return productRepository.save(product);
  }

  public Product updateProduct(Product product) {
    log.info("updateProduct invoked ");
    Product existingProduct =
        getProductById(product.getId()); // Ensure product exists before updating
    existingProduct.setName(product.getName()); // Update relevant fields
    existingProduct.setDescription(product.getDescription());
    existingProduct.setPrice(product.getPrice());
    existingProduct.setStock(product.getStock());
    return productRepository.save(existingProduct);
  }

  public void deleteProduct(Long id) {
    log.info("deleteProduct invoked ");

    productRepository.deleteById(id);
  }

  public Product findProductById(Long id) {
    log.info("findProductById invoked " + id.toString());

    return productRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
  }
}
