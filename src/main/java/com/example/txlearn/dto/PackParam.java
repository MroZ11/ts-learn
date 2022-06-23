package com.example.txlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackParam {

    private String customer;

    private String address;

    private List<OrderParam> orderList;

}
