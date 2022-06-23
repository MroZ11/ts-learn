package com.example.txlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderParam {

    private String name;

    private BigDecimal unitPrice;

    private Integer number;

    private String customer;

}
