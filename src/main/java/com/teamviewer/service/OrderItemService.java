package com.teamviewer.service;

import com.teamviewer.entity.OrderItem;
import com.teamviewer.exception.ResourceNotFoundException;
import com.teamviewer.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemService {

  private final OrderItemRepository orderItemRepository;

  public List<OrderItem> findAllOrderItems() {
    log.info("findAllOrderItems invoked");
    return orderItemRepository.findAll();
  }

  public OrderItem findOrderItemById(Long id) {
    log.info("findOrderItemById invoked" + id);

    return orderItemRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + id));
  }

  public OrderItem createOrderItem(OrderItem orderItem) {
    log.info("createOrderItem invoked");

    return orderItemRepository.save(orderItem);
  }

  public OrderItem updateOrderItem(OrderItem orderItem) {
    log.info("updateOrderItem invoked");

    OrderItem existingOrderItem =
        findOrderItemById(orderItem.getId()); // Ensure order item exists before updating
    // Update relevant fields
    existingOrderItem.setProductId(orderItem.getProductId());
    existingOrderItem.setQuantity(orderItem.getQuantity());
    existingOrderItem.setUnitPrice(orderItem.getUnitPrice());
    return orderItemRepository.save(existingOrderItem);
  }

  public void deleteOrderItem(Long id) {
    log.info("deleteOrderItem invoked" + id);

    orderItemRepository.deleteById(id);
  }
}
