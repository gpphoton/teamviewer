package com.teamviewer.service;

import com.teamviewer.entity.Product;
import com.teamviewer.exception.ResourceNotFoundException;
import com.teamviewer.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {

  private ProductRepository productRepository;

  private ProductService productService;

  @BeforeAll
  void setUp() {
    productRepository = Mockito.mock(ProductRepository.class);
    productService = new ProductService(productRepository);
  }

  @AfterEach
  void afterEach() {
    reset(productRepository);
  }

  @Test
  public void shouldSaveProduct() {
    // Arrange
    Long id = 1L;
    Product product =
        Product.builder()
            .id(id)
            .description("test")
            .name("test-name")
            .price(BigDecimal.TEN)
            .stock(10)
            .build();
    // Set product data

    // Act
    productService.createProduct(product);

    // Assert
    verify(productRepository).save(product);
  }

  @Test
  public void shouldFindProductById() {
    // Arrange
    Long id = 1L;
    Product expectedProduct =
        Product.builder()
            .id(id)
            .description("test")
            .name("test-name")
            .price(BigDecimal.TEN)
            .stock(10)
            .build();
    expectedProduct.setId(id);

    Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(expectedProduct));

    // Act
    Product actualProduct = productService.findProductById(id);

    // Assert
    assertNotNull(actualProduct);
    assertEquals(expectedProduct, actualProduct);
  }

  @Test
  public void shouldThrowExceptionWhenProductNotFound() {
    // Arrange
    Long id = 1L;
    Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

    // Act
    // Expected exception handling

    // Assert
    assertThrows(ResourceNotFoundException.class, () -> productService.findProductById(id));
  }

  @Test
  public void shouldUpdateProduct() {
    // Arrange
    Long id = 1L;
    Product existingProduct =
        Product.builder()
            .id(id)
            .description("test")
            .name("test-name")
            .price(BigDecimal.TEN)
            .stock(10)
            .build();
    existingProduct.setId(id);
    // Set other existingProduct data

    Product updatedProduct =
        Product.builder()
            .id(id)
            .description("test-changed")
            .name("test-name-changed")
            .price(BigDecimal.ONE)
            .stock(10)
            .build();
    updatedProduct.setId(id);
    // Set updated data

    Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
    Mockito.when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

    // Act
    Product actualProduct = productService.updateProduct(updatedProduct);

    // Assert
    verify(productRepository).findById(id);
    verify(productRepository).save(updatedProduct);
    assertEquals(updatedProduct, actualProduct);
  }

  @Test
  public void shouldDeleteProduct() {
    // Arrange
    Long id = 1L;

    // Act
    productService.deleteProduct(id);

    // Assert
    verify(productRepository).deleteById(id);
  }

  @Test
  public void shouldGetAllProducts() {
    // Arrange
    List<Product> expectedProducts =
        Collections.singletonList(
            Product.builder()
                .id(1l)
                .description("test")
                .name("test-name")
                .price(BigDecimal.TEN)
                .stock(10)
                .build());
    // Add products to expectedProducts

    Mockito.when(productRepository.findAll()).thenReturn(expectedProducts);

    // Act
    List<Product> actualProducts = productService.findAllProducts();

    // Assert
    assertEquals(expectedProducts, actualProducts);
  }
}
