package com.teamviewer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_item")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer orderId;

  private Integer productId;

  private Integer quantity;

  private BigDecimal unitPrice;
}
