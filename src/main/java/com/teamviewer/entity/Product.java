package com.teamviewer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_product")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

  private Integer stock;

  private BigDecimal price;
}
