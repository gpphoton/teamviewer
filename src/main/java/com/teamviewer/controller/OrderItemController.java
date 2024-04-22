package com.teamviewer.controller;

import com.teamviewer.entity.OrderItem;
import com.teamviewer.service.OrderItemService;
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
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

  private final OrderItemService orderItemService;

  @GetMapping
  @Operation(summary = "Get all order items", tags = "Order Items")
  @ApiResponse(
      responseCode = "200",
      description = "Successful operation",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(ref = "#/components/schemas/OrderItem")))
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  public List<OrderItem> getAllOrderItems() {
    log.info("getAllOrderItems invoked ");
    MDC.clear();
    MDC.put(API_NAME, GET_ALL_ORDER_ITEM_API);
    return orderItemService.findAllOrderItems();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get an order item by ID", tags = "Order Items")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(ref = "#/components/schemas/OrderItem"))),
        @ApiResponse(responseCode = "404", description = "Order item not found")
      })
  public OrderItem getOrderItemById(@PathVariable Long id) {
    MDC.clear();
    MDC.put(API_NAME, GET_ORDER_ITEM_BY_ID);
    MDC.put(ORDER_ITEM_ID, id.toString());
    log.info("getOrderItemById invoked " + id);
    return orderItemService.findOrderItemById(id);
  }

  @PostMapping
  @Operation(summary = "Create a new order item", tags = "Order Items")
  @ApiResponse(
      responseCode = "200",
      description = "Order item created successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(ref = "#/components/schemas/OrderItem")))
  @ApiResponse(responseCode = "400", description = "Invalid request body")
  public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {
    MDC.clear();
    MDC.put(API_NAME, CREATE_ORDER_ITEM_API);
    MDC.put(ORDER_ITEM_DETAILS, orderItem.toString());
    log.info("createOrderItem invoked " + orderItem);
    return orderItemService.createOrderItem(orderItem);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing order item", tags = "Order Items")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Order item updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(ref = "#/components/schemas/OrderItem"))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "Order item not found")
      })
  public OrderItem updateOrderItem(@PathVariable Long id, @RequestBody OrderItem orderItem) {
    MDC.clear();
    MDC.put(API_NAME, UPDATE_ORDER_ITEM_API);
    MDC.put(ORDER_ITEM_ID, id.toString());
    log.info("updateOrderItem invoked for id " + id + " with updatedOrderItem " + orderItem);
    orderItem.setId(id); // Ensure ID matches path variable
    return orderItemService.updateOrderItem(orderItem);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an order item by ID", tags = "Order Items")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Order item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Order item not found")
      })
  public void deleteOrderItem(@PathVariable Long id) {
    MDC.clear();
    MDC.put(API_NAME, DELETE_ORDER_ITEM_API);
    MDC.put(ORDER_ITEM_ID, id.toString());

    log.info("deleteOrderItem invoked for id " + id);

    orderItemService.deleteOrderItem(id);
  }
}
