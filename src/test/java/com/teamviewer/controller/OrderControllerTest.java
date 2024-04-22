package com.teamviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamviewer.entity.Order;
import com.teamviewer.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
class OrderControllerTest {

  private OrderService orderService;

  private OrderController orderController;

  private ObjectMapper objectMapper;

  @BeforeAll
  void setUp() {
    orderService = Mockito.mock(OrderService.class);
    orderController = new OrderController(orderService);
    objectMapper = new ObjectMapper();
  }

  @AfterEach
  void afterEach() {
    reset(orderService);
  }

  @Test
  void shouldReturnAllOrders() throws Exception {
    // Arrange
    Long id = 1L;
    List<Order> expectedOrders =
        Collections.singletonList(
            Order.builder().id(id).totalamount(BigDecimal.TEN).build()); // Set up expected orders
    Mockito.when(orderService.findAllOrders()).thenReturn(expectedOrders);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    MvcResult result = mockMvc.perform(get("/api/orders")).andExpect(status().isOk()).andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    List actualOrders =
        objectMapper.readValue(jsonResponse, List.class); // Extract orders from response
    assertEquals(expectedOrders.size(), actualOrders.size());
  }

  @Test
  void shouldReturnOrderById() throws Exception {
    // Arrange
    Long id = 1L;
    Order expectedOrder =
        Order.builder().id(id).totalamount(BigDecimal.TEN).build(); // Set up expected order
    Mockito.when(orderService.findOrderById(id)).thenReturn(expectedOrder);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    MvcResult result =
        mockMvc.perform(get("/api/orders/{id}", id)).andExpect(status().isOk()).andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    Order actualOrder =
        objectMapper.readValue(jsonResponse, Order.class); // Extract order from response
    assertEquals(expectedOrder, actualOrder);
  }

  @Test
  void shouldCreateOrder() throws Exception {
    // Arrange
    Long id = 1L;
    Order orderCreate =
        Order.builder().id(id).totalamount(BigDecimal.TEN).build(); // Set up order create data
    Order expectedOrder = Order.builder().id(id).build();
    // Set up expected order
    Mockito.when(orderService.createOrder(orderCreate)).thenReturn(expectedOrder);

    String jsonString = objectMapper.writeValueAsString(orderCreate);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    MvcResult result =
        mockMvc
            .perform(
                post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(jsonString))
            .andExpect(status().isOk())
            .andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    Order actualOrder =
        objectMapper.readValue(jsonResponse, Order.class); // Extract order from response
    assertEquals(expectedOrder, actualOrder);
  }

  @Test
  void shouldUpdateOrder() throws Exception {
    // Arrange
    Long id = 1L;
    Order orderUpdate =
        Order.builder().id(id).totalamount(BigDecimal.TEN).build(); // Set up order update data
    Order expectedOrder =
        Order.builder().id(id).totalamount(BigDecimal.ONE).build(); // Set up expected order
    Mockito.when(orderService.updateOrder(orderUpdate)).thenReturn(expectedOrder);
    String jsonString = objectMapper.writeValueAsString(orderUpdate);
    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    MvcResult result =
        mockMvc
            .perform(
                put("/api/orders/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonString))
            .andExpect(status().isOk())
            .andReturn();

    // Assert
    String jsonResponse = result.getResponse().getContentAsString();
    Order actualOrder =
        objectMapper.readValue(jsonResponse, Order.class); // Extract order from response
    assertEquals(expectedOrder, actualOrder);
  }

  @Test
  void shouldDeleteOrder() throws Exception {
    // Arrange
    Long id = 1L;
    Mockito.doNothing().when(orderService).deleteOrder(id);

    // Act
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    mockMvc.perform(delete("/api/orders/{id}", id)).andExpect(status().isOk());

    // Assert
    Mockito.verify(orderService, times(1)).deleteOrder(id);
  }
}
