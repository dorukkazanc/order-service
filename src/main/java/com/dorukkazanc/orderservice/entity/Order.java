/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.entity;

import com.dorukkazanc.orderservice.enums.OrderSide;
import com.dorukkazanc.orderservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@Entity
@Table(name = "orders")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Order extends AuditableEntity {
    @Column(name= "customer_id")
    private String customerId;
    @Column(name= "asset_name")
    private String assetName;
    @Column(name= "order_side")
    @Enumerated(EnumType.STRING)  // ✅ This is required!
    private OrderSide orderSide;
    private Long size;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)  // ✅ This is required!
    private OrderStatus status;
}
