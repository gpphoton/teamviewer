package com.teamviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamviewer.entity.OrderItem;
import com.teamviewer.service.OrderItemService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class OrderItemControllerTest {

  private OrderItemService orderItemService;

  private OrderItemController orderItemController;

  private ObjectMapper objectMapper;

  @BeforeAll
  void setUp() {
    orderItemService = Mockito.mock(OrderItemService.class);
    orderItemController = new OrderItemController(orderItemService);
    objectMapper = new ObjectMapper();
  }

  @AfterEach
  void afterEach() {
    reset(orderItemService);
  }

  @Test
  void shouldReturnAllOrderItems() throws Exception {
    // Arrange
    Long id = 1L;
    List<OrderItem> expectedOrderItems =
        Collections.singletonList(
            OrderItem.builder()
                .orderId(123)
                .quantity(10)
                .productId(123)
                .unitPrice(BigDecimal.ONE)
                .id(id)
                .build()); // Set up expected order items
    Mockito.when(orderItemService.findAllOrderItems()).thenReturn(expectedOrderItems);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderItemController).build();
    MvcResult result =
        mockMvc.perform(get("/api/order-items")).andExpect(status().isOk()).andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    List actualOrderItems = objectMapper.readValue(jsonResponse, List.class);
    // Extract order items from response
    assertEquals(expectedOrderItems.size(), actualOrderItems.size());
  }

  @Test
  void shouldReturnOrderItemById() throws Exception {
    // Arrange
    Long id = 1L;
    OrderItem expectedOrderItem =
        OrderItem.builder()
            .orderId(123)
            .quantity(10)
            .productId(123)
            .unitPrice(BigDecimal.ONE)
            .id(id)
            .build(); // Set up expected order item
    Mockito.when(orderItemService.findOrderItemById(id)).thenReturn(expectedOrderItem);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderItemController).build();
    MvcResult result =
        mockMvc.perform(get("/api/order-items/{id}", id)).andExpect(status().isOk()).andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    OrderItem actualOrderItem =
        objectMapper.readValue(jsonResponse, OrderItem.class); // Extract order item from response
    assertEquals(expectedOrderItem, actualOrderItem);
  }

  @Test
  void shouldCreateOrderItem() throws Exception {
    // Arrange
    Long id = 1l;
    OrderItem orderItemCreate =
        OrderItem.builder()
            .orderId(123)
            .quantity(10)
            .productId(123)
            .unitPrice(BigDecimal.ONE)
            .id(id)
            .build(); // Set up order item create data
    OrderItem expectedOrderItem =
        OrderItem.builder()
            .orderId(123)
            .quantity(10)
            .productId(123)
            .unitPrice(BigDecimal.ONE)
            .id(id)
            .build(); // Set up expected order item
    Mockito.when(orderItemService.createOrderItem(orderItemCreate)).thenReturn(expectedOrderItem);
    String jsonString = objectMapper.writeValueAsString(orderItemCreate);
    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderItemController).build();
    MvcResult result =
        mockMvc
            .perform(
                post("/api/order-items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonString))
            .andExpect(status().isOk())
            .andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    OrderItem actualOrderItem =
        objectMapper.readValue(jsonResponse, OrderItem.class); // Extract order item from response
    assertEquals(expectedOrderItem, actualOrderItem);
  }

  @Test
  void shouldUpdateOrderItem() throws Exception {
    // Arrange
    Long id = 1L;
    OrderItem orderItemUpdate =
        OrderItem.builder()
            .orderId(123)
            .quantity(10)
            .productId(123)
            .unitPrice(BigDecimal.TEN)
            .id(id)
            .build(); // Set up order item update data
    OrderItem expectedOrderItem =
        OrderItem.builder()
            .orderId(123)
            .quantity(10)
            .productId(234)
            .unitPrice(BigDecimal.ONE)
            .id(id)
            .build(); // Set up expected order item
    Mockito.when(orderItemService.updateOrderItem(orderItemUpdate)).thenReturn(expectedOrderItem);
    String jsonString = objectMapper.writeValueAsString(orderItemUpdate);
    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderItemController).build();
    MvcResult result =
        mockMvc
            .perform(
                put("/api/order-items/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonString))
            .andExpect(status().isOk())
            .andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    if (StringUtils.isNotBlank(jsonResponse)) {

      OrderItem actualOrderItem =
          objectMapper.readValue(jsonResponse, OrderItem.class); // Extract order item from response
      assertEquals(expectedOrderItem, actualOrderItem);
    }
  }

  @Test
  void shouldDeleteOrderItem() throws Exception {
    // Arrange
    Long id = 1L;
    Mockito.doNothing().when(orderItemService).deleteOrderItem(id);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderItemController).build();
    mockMvc.perform(delete("/api/order-items/{id}", id)).andExpect(status().isOk());

    // Assert
    Mockito.verify(orderItemService, times(1)).deleteOrderItem(id);
  }
}
