package com.example.txlearn.controller;

import com.example.txlearn.dto.OrderParam;
import com.example.txlearn.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("add")
    public ResponseEntity<Object> add(@RequestBody OrderParam orderParam) {
        orderService.createOrder(orderParam);
        return ResponseEntity.ok(null);
    }


    @PostMapping("addBatch")
    public ResponseEntity<Object> addBatch(@RequestBody List<OrderParam> orderParam) {
        orderService.createOrderBatch(orderParam);
        return ResponseEntity.ok(null);
    }

    @PostMapping("addBatchTx")
    public ResponseEntity<Object> addBatchTx(@RequestBody List<OrderParam> orderParam) {
        orderService.createOrderBatchTx(orderParam);
        return ResponseEntity.ok(null);
    }

    @PostMapping("addBatchTx2")
    public ResponseEntity<Object> addBatchTx2(@RequestBody List<OrderParam> orderParam) {
        orderService.createOrderBatchTx2(orderParam);
        return ResponseEntity.ok(null);
    }

    @PostMapping("addAndLog")
    public ResponseEntity<Object> addAndLog(@RequestBody OrderParam orderParam) {
        orderService.createOrderAndLog(orderParam);
        return ResponseEntity.ok(null);
    }

    @PostMapping("addBatchAndLogWithTx")
    public ResponseEntity<Object> addAndLog(@RequestBody List<OrderParam> orderParam) {
        orderService.addBatchAndLogWithTx(orderParam);
        return ResponseEntity.ok(null);
    }








}
