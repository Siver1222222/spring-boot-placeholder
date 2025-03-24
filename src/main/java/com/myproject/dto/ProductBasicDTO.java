package com.myproject.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBasicDTO {
    private Long id;
    private String name;
    private double price;
}