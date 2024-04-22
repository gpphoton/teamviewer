package com.teamviewer.controller;

import com.teamviewer.entity.Product;
import com.teamviewer.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.teamviewer.util.Constants.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  @Operation(description = "Get all products", tags = "Products")
  @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(ref = "#/components/schemas/Product")))
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  public List<Product> getAllProducts() {
    MDC.clear();
    MDC.put(API_NAME, GET_ALL_PRODUCTS_API);
    log.info("getAllProducts invoked  ");

    return productService.findAllProducts();
  }

  @GetMapping("/{id}")
  @Operation(description = "Get a product by ID", tags = "Products")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(ref = "#/components/schemas/Product"))),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  public Product getProductById(@PathVariable Long id) {
    MDC.clear();
    MDC.put(API_NAME, GET_PRODUCTS_BY_ID);
    MDC.put(PRODUCT_ID, id.toString());
    log.info("getProductById invoked  " + id);
    return productService.findProductById(id);
  }

  @PostMapping
  @Operation(description = "Create a new product", tags = "Products")
  @ApiResponse(
      responseCode = "200",
      description = "Product created successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(ref = "#/components/schemas/Product")))
  @ApiResponse(responseCode = "400", description = "Invalid request body")
  public Product createProduct(@RequestBody Product product) {
    MDC.clear();
    MDC.put(API_NAME, CREATE_PRODUCT_API);
    MDC.put(PRODUCT_DETAILS, product.toString());
    log.info("createProduct invoked  " + product);
    return productService.createProduct(product);
  }

  @PutMapping("/{id}")
  @Operation(description = "Update an existing product", tags = "Products")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(ref = "#/components/schemas/Product"))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
    MDC.clear();
    MDC.put(API_NAME, UPDATE_PRODUCT_API);
    MDC.put(PRODUCT_ID, id.toString());
    product.setId(id); // Ensure ID matches path variable
    log.info("updateProduct invoked  " + id + " product " + product);
    return productService.updateProduct(product);
  }

  @DeleteMapping("/{id}")
  @Operation(description = "Delete a product by ID", tags = "Products")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  public void deleteProduct(@PathVariable Long id) {
    MDC.clear();
    MDC.put(API_NAME, DELETE_PRODUCT_API);
    MDC.put(PRODUCT_ID, id.toString());
    log.info("deleteProduct invoked  " + id);
    productService.deleteProduct(id);
  }
}
