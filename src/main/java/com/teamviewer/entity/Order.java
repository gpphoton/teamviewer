package com.teamviewer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_order")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal totalamount;
}
