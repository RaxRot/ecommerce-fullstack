package com.raxrot.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String image;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal specialPrice;
    private BigDecimal discount;
}
