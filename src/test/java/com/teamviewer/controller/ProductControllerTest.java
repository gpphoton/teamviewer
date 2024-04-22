package com.teamviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamviewer.entity.Product;
import com.teamviewer.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest {

  private ProductService productService;

  private ProductController productController;
  private ObjectMapper objectMapper;

  @BeforeAll
  void setUp() {
    productService = Mockito.mock(ProductService.class);
    productController = new ProductController(productService);
    objectMapper = new ObjectMapper();
  }

  @AfterEach
  void afterEach() {
    reset(productService);
  }

  @Test
  void shouldReturnAllProducts() throws Exception {
    // Arrange
    Long id = 1L;
    Long id2 = 1L;
    List<Product> expectedProducts =
        Arrays.asList(
            Product.builder().id(id).name("test-name").description("test-description").build(),
            Product.builder()
                .id(id2)
                .name("test-name-2")
                .description("test-description-2")
                .build()); // Set up expected products
    Mockito.when(productService.findAllProducts()).thenReturn(expectedProducts);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    MvcResult result = mockMvc.perform(get("/api/products")).andExpect(status().isOk()).andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    List actualProducts = objectMapper.readValue(jsonResponse, List.class);
    assertEquals(expectedProducts.size(), actualProducts.size());
  }

  @Test
  void shouldReturnProductById() throws Exception {
    // Arrange
    Long id = 1L;
    Product expectedProduct =
        Product.builder()
            .id(id)
            .name("test-name")
            .description("test-description")
            .build(); // Set up expected product
    Mockito.when(productService.findProductById(id)).thenReturn(expectedProduct);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    MvcResult result =
        mockMvc.perform(get("/api/products/{id}", id)).andExpect(status().isOk()).andReturn();

    // Assert
    String jsonResponse =
        result.getResponse().getContentAsString(); // Extract product from response
    Product actualProduct = objectMapper.readValue(jsonResponse, Product.class);
    assertEquals(expectedProduct, actualProduct);
  }

  @Test
  void shouldCreateProduct() throws Exception {
    // Arrange
    Long id = 1l;
    Product productCreate =
        Product.builder()
            .id(id)
            .description("test-description")
            .name("test-name")
            .build(); // Set up product create data
    Product expectedProduct =
        Product.builder()
            .id(id)
            .description("test-description")
            .name("test-name")
            .build(); // Set up expected product
    Mockito.when(productService.createProduct(productCreate)).thenReturn(expectedProduct);
    String jsonString = objectMapper.writeValueAsString(productCreate);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    MvcResult result =
        mockMvc
            .perform(
                post("/api/products").contentType(MediaType.APPLICATION_JSON).content(jsonString))
            .andExpect(status().isOk())
            .andReturn();

    // Assert
    // Extract product from response
    String jsonResponse =
        result.getResponse().getContentAsString(); // Extract product from response
    Product actualProduct = objectMapper.readValue(jsonResponse, Product.class);
    assertEquals(expectedProduct, actualProduct);
  }

  @Test
  void shouldUpdateProduct() throws Exception {
    // Arrange
    Long id = 1L;
    Product productUpdate = Product.builder().build(); // Set up product update data
    Product expectedProduct = Product.builder().build(); // Set up expected product
    Mockito.when(productService.updateProduct(productUpdate)).thenReturn(expectedProduct);

    String jsonString = objectMapper.writeValueAsString(productUpdate);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    MvcResult result =
        mockMvc
            .perform(
                put("/api/products/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonString))
            .andExpect(status().isOk())
            .andReturn();

    // Assert
    String jsonResponse =
        result.getResponse().getContentAsString(); // Extract product from response
    if (StringUtils.isNotBlank(jsonResponse)) {
      Product actualProduct = objectMapper.readValue(jsonResponse, Product.class);
      // Extract product from response
      assertEquals(expectedProduct, actualProduct);
    }
  }

  @Test
  void shouldDeleteProduct() throws Exception {
    // Arrange
    Long id = 1L;
    Mockito.doNothing().when(productService).deleteProduct(id);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    mockMvc.perform(delete("/api/products/{id}", id)).andExpect(status().isOk());

    // Assert
    Mockito.verify(productService, times(1)).deleteProduct(id);
  }
}
