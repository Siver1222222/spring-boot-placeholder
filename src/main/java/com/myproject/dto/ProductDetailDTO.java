package com.myproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDetailDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    // Add additional fields as needed
}