package com.teamviewer.service;

import com.teamviewer.entity.Order;
import com.teamviewer.exception.ResourceNotFoundException;
import com.teamviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public List<Order> findAllOrders() {
    return orderRepository.findAll();
  }

  public Order findOrderById(Long id) {
    log.info("findOrderById " + id);
    return orderRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
  }

  public Order createOrder(Order order) {
    log.info("createOrder " + order);
    return orderRepository.save(order);
  }

  public Order updateOrder(Order order) {
    Order existingOrder = findOrderById(order.getId()); // Ensure order exists before updating
    log.info("updateOrder invoked " + existingOrder);
    // update existingOrder with order
    existingOrder.setTotalamount(order.getTotalamount());
    return orderRepository.save(existingOrder);
  }

  public void deleteOrder(Long id) {
    log.info("deleteOrder invoked for " + id);
    orderRepository.deleteById(id);
  }
}
