package com.example.txlearn.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class ReqLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String type;

    private Boolean success;

    private String reqBody;

    private String repBody;

    private String fromBy;
}
