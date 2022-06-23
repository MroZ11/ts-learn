package com.example.txlearn.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Cus_Pack")
@Data
public class Pack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private String customer;


    private String address;

    @OneToMany
    @JoinColumn(name="pack_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<Order> orderList;


}
