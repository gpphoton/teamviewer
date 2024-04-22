package com.teamviewer.controller;

import com.teamviewer.entity.Order;
import com.teamviewer.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.teamviewer.util.Constants.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  @Operation(description = "Get all orders", tags = "Orders")
  @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(ref = "#/components/schemas/Order")))
  public List<Order> getAllOrders() {
    MDC.clear();
    MDC.put(API_NAME, GET_ALL_ORDERS_API);
    log.info("getAllOrders invoked ");
    return orderService.findAllOrders();
  }

  @GetMapping("/{id}")
  @Operation(description = "Get an order by ID", tags = "Orders")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(ref = "#/components/schemas/Order"))),
        @ApiResponse(responseCode = "404", description = "Order not found")
      })
  public Order getOrderById(@PathVariable Long id) {
    MDC.clear();
    MDC.put(API_NAME, GET_ORDER_BY_ID);
    MDC.put(ORDER_ID, id.toString());
    log.info("getOrderById invoked ");
    return orderService.findOrderById(id);
  }

  @PostMapping
  @Operation(description = "Create a new order", tags = "Orders")
  @ApiResponse(
      responseCode = "200",
      description = "Order created successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(ref = "#/components/schemas/Order")))
  @ApiResponse(responseCode = "400", description = "Invalid request body")
  public Order createOrder(@RequestBody Order order) {
    MDC.clear();
    MDC.put(API_NAME, CREATE_ORDER_API);
    MDC.put(ORDER_DETAILS, order.toString());
    log.info("createOrder invoked ");
    return orderService.createOrder(order);
  }

  @PutMapping("/{id}")
  @Operation(description = "Update an existing order", tags = "Orders")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Order updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(ref = "#/components/schemas/Order"))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "Order not found")
      })
  public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
    MDC.clear();
    MDC.put(API_NAME, UPDATE_ORDER_API);
    MDC.put(ORDER_ID, id.toString());
    MDC.put(ORDER_DETAILS, order.toString());
    log.info("updateOrder invoked ");
    order.setId(id); // Ensure ID matches path variable
    return orderService.updateOrder(order);
  }

  @DeleteMapping("/{id}")
  @Operation(description = "Delete an order by ID", tags = "Orders")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found")
      })
  public void deleteOrder(@PathVariable Long id) {
    MDC.clear();
    MDC.put(API_NAME, DELETE_ORDER_API);
    MDC.put(ORDER_ID, id.toString());
    log.info("deleteOrder invoked ");
    orderService.deleteOrder(id);
  }
}
