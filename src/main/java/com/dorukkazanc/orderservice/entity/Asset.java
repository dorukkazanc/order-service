/**
 * Author: doruk.kazanc
 * Date: 22.08.2025
 */

package com.dorukkazanc.orderservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assets")
public class Asset extends AuditableEntity{
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "asset_name")
    private String assetName;
    private Long size;
    @Column(name = "usable_size")
    private Long usableSize;
}
