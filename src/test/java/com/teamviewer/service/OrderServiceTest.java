package com.teamviewer.service;

import com.teamviewer.entity.Order;
import com.teamviewer.exception.ResourceNotFoundException;
import com.teamviewer.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class OrderServiceTest {

  private OrderRepository orderRepository;

  @Mock private OrderService orderService;

  @BeforeAll
  void setUp() {
    orderRepository = Mockito.mock(OrderRepository.class);
    orderService = new OrderService(orderRepository);
  }

  @AfterEach
  void afterEach() {
    reset(orderRepository);
  }

  @Test
  public void shouldSaveOrder() {
    // Arrange
    Order order = Order.builder().totalamount(BigDecimal.TEN).id(1l).build();
    // Set order data

    // Act
    orderService.createOrder(order);

    // Assert
    verify(orderRepository).save(order);
  }

  @Test
  public void shouldFindOrderById() {
    // Arrange
    Long id = 1L;
    Order expectedOrder = Order.builder().totalamount(BigDecimal.TEN).id(id).build();
    expectedOrder.setId(id);

    Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(expectedOrder));

    // Act
    Order actualOrder = orderService.findOrderById(id);

    // Assert
    assertNotNull(actualOrder);
    assertEquals(expectedOrder, actualOrder);
  }

  @Test
  public void shouldThrowExceptionWhenOrderNotFound() {
    // Arrange
    Long id = 1L;
    Mockito.when(orderRepository.findById(id)).thenReturn(Optional.empty());

    // Act
    // Expected exception handling

    // Assert
    assertThrows(ResourceNotFoundException.class, () -> orderService.findOrderById(id));
  }

  @Test
  public void shouldUpdateOrder() {
    // Arrange
    Long id = 1L;
    Order existingOrder = Order.builder().totalamount(BigDecimal.TEN).id(id).build();
    existingOrder.setId(id);
    // Set other existingOrder data

    Order updatedOrder = Order.builder().totalamount(BigDecimal.ONE).id(id).build();
    updatedOrder.setId(id);
    // Set updated data

    Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));
    Mockito.when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);

    // Act
    Order actualOrder = orderService.updateOrder(updatedOrder);

    // Assert
    //    verify(orderItemRepository).findById(id);
    verify(orderRepository).save(updatedOrder);
    assertEquals(updatedOrder, actualOrder);
  }

  @Test
  public void shouldDeleteOrder() {
    // Arrange
    Long id = 1L;

    // Act
    orderService.deleteOrder(id);

    // Assert
    verify(orderRepository).deleteById(id);
  }

  @Test
  public void shouldGetAllOrders() {
    // Arrange
    List<Order> expectedOrders = new ArrayList<>();
    // Add orders to expectedOrders

    Mockito.when(orderRepository.findAll()).thenReturn(expectedOrders);

    // Act
    List<Order> actualOrders = orderService.findAllOrders();

    // Assert
    assertEquals(expectedOrders, actualOrders);
  }
}
