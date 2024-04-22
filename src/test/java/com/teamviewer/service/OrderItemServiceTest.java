package com.teamviewer.service;

import com.teamviewer.entity.OrderItem;
import com.teamviewer.exception.ResourceNotFoundException;
import com.teamviewer.repository.OrderItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class OrderItemServiceTest {

  private OrderItemRepository orderItemRepository;

  private OrderItemService orderItemService;

  @BeforeAll
  void setUp() {
    orderItemRepository = Mockito.mock(OrderItemRepository.class);
    orderItemService = new OrderItemService(orderItemRepository);
  }

  @AfterEach
  void afterEach() {
    reset(orderItemRepository);
  }

  @Test
  public void shouldSaveOrderItem() {
    // Arrange
    OrderItem orderItem =
        OrderItem.builder()
            .orderId(123)
            .productId(123)
            .quantity(123)
            .unitPrice(BigDecimal.TEN)
            .build();
    // Set orderItem data

    // Act
    orderItemService.createOrderItem(orderItem);

    // Assert
    verify(orderItemRepository).save(orderItem);
  }

  @Test
  public void shouldFindOrderItemById() {
    // Arrange
    Long id = 1L;
    OrderItem expectedOrderItem =
        OrderItem.builder()
            .orderId(123)
            .productId(123)
            .quantity(123)
            .unitPrice(BigDecimal.TEN)
            .build();
    expectedOrderItem.setId(id);

    when(orderItemRepository.findById(id)).thenReturn(Optional.of(expectedOrderItem));

    // Act
    OrderItem actualOrderItem = orderItemService.findOrderItemById(id);

    // Assert
    assertNotNull(actualOrderItem);
    assertEquals(expectedOrderItem, actualOrderItem);
  }

  @Test
  public void shouldThrowExceptionWhenOrderItemNotFound() {
    // Arrange
    Long id = 1L;
    when(orderItemRepository.findById(id)).thenReturn(Optional.empty());

    // Act
    // Expected exception handling

    // Assert
    assertThrows(ResourceNotFoundException.class, () -> orderItemService.findOrderItemById(id));
  }

  @Test
  public void shouldUpdateOrderItem() {
    // Arrange
    Long id = 1L;
    OrderItem existingOrderItem =
        OrderItem.builder()
            .orderId(123)
            .productId(123)
            .quantity(123)
            .unitPrice(BigDecimal.TEN)
            .build();
    existingOrderItem.setId(id);
    // Set other existingOrderItem data

    OrderItem updatedOrderItem =
        OrderItem.builder()
            .orderId(123)
            .productId(345)
            .quantity(145)
            .unitPrice(BigDecimal.ONE)
            .build();
    updatedOrderItem.setId(id);
    // Set updated data

    when(orderItemRepository.findById(id)).thenReturn(Optional.of(existingOrderItem));
    when(orderItemRepository.save(updatedOrderItem)).thenReturn(updatedOrderItem);

    // Act
    OrderItem actualOrderItem = orderItemService.updateOrderItem(updatedOrderItem);

    // Assert
    verify(orderItemRepository).findById(id);
    verify(orderItemRepository).save(updatedOrderItem);
    assertEquals(updatedOrderItem, actualOrderItem);
  }

  @Test
  public void shouldDeleteOrderItem() {
    // Arrange
    Long id = 1L;

    // Act
    orderItemService.deleteOrderItem(id);

    // Assert
    verify(orderItemRepository).deleteById(id);
  }

  @Test
  void findAllOrderItems() {
    List<OrderItem> expectedOrderItems = List.of(new OrderItem());
    when(orderItemRepository.findAll()).thenReturn(expectedOrderItems);

    List<OrderItem> actualOrderItems = orderItemService.findAllOrderItems();

    assertEquals(expectedOrderItems, actualOrderItems);
  }

  @Test
  void findOrderItemById_found() {
    Long id = 1L;
    OrderItem expectedOrderItem = new OrderItem();
    when(orderItemRepository.findById(id)).thenReturn(Optional.of(expectedOrderItem));

    OrderItem actualOrderItem = orderItemService.findOrderItemById(id);

    assertEquals(expectedOrderItem, actualOrderItem);
  }

  @Test
  void findOrderItemById_notFound() {
    Long id = 1L;
    when(orderItemRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> orderItemService.findOrderItemById(id));
  }

  @Test
  void createOrderItem() {
    OrderItem orderItem = new OrderItem();
    when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

    OrderItem savedOrderItem = orderItemService.createOrderItem(orderItem);

    assertEquals(orderItem, savedOrderItem);
  }

  @Test
  void updateOrderItem() {
    Long id = 1L;
    OrderItem updatedOrderItem = new OrderItem();
    updatedOrderItem.setId(id);
    when(orderItemRepository.findById(id)).thenReturn(Optional.of(updatedOrderItem));
    when(orderItemRepository.save(updatedOrderItem)).thenReturn(updatedOrderItem);

    OrderItem savedOrderItem = orderItemService.updateOrderItem(updatedOrderItem);

    assertEquals(updatedOrderItem, savedOrderItem);
  }

  @Test
  void deleteOrderItem() {
    Long id = 1L;
    orderItemService.deleteOrderItem(id);

    verify(orderItemRepository, times(1)).deleteById(id);
  }
}
