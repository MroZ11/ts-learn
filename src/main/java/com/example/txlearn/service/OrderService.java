package com.example.txlearn.service;

import com.alibaba.fastjson2.JSON;
import com.example.txlearn.dao.OrderRepository;
import com.example.txlearn.dto.OrderParam;
import com.example.txlearn.entity.Order;
import com.example.txlearn.entity.ReqLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReqLogService reqLogService;

    public Order createOrder(OrderParam param) {
        orderParamVerify(param);

        Order order = new Order();
        order.setName(param.getName());
        order.setNumber(param.getNumber());
        order.setCustomer(param.getCustomer());
        order.setUnitPrice(param.getUnitPrice());
        return orderRepository.saveAndFlush(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrderTx(OrderParam param) {
        createOrder(param);
    }


    public void createOrderBatch(List<OrderParam> list) {
        for (int i = 0; i < list.size(); i++) {
            OrderParam param = list.get(i);
            createOrder(param);
        }
    }



    private void orderParamVerify(OrderParam param){
        Assert.hasText(param.getName(),String.format("%s can't empty","OrderParam::Name"));
        Assert.hasText(param.getCustomer(),String.format("%s can't empty","OrderParam::Customer"));
        Assert.notNull(param.getNumber(),String.format("%s can't empty","OrderParam::Quantity"));
        Assert.notNull(param.getUnitPrice(),String.format("%s can't empty","OrderParam::UnitPrice"));
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Order>  createOrderBatchTx(List<OrderParam> list) {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            OrderParam param = list.get(i);
            orderList.add(createOrder(param));
        }

        return orderList;

    }

    public void createOrderBatchTx2(List<OrderParam> list) {
        for (int i = 0; i < list.size(); i++) {
            OrderParam param = list.get(i);
            try {
                createOrderTx(param);
            }catch (Exception e){

            }
        }
    }


    public void createOrderAndLog(OrderParam orderParam) {
        ReqLog reqLog = new ReqLog();
        reqLog.setSuccess(true);
        reqLog.setFromBy(orderParam.getCustomer());
        reqLog.setReqBody(JSON.toJSONString(orderParam));
        reqLog.setType("createOrder");
        try {
            createOrderTx(orderParam);
        }catch (Exception e){
            reqLog.setSuccess(false);
            reqLog.setRepBody(e.getMessage());
        }finally {
           reqLogService.saveReqLog(reqLog);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void addBatchAndLogWithTx(List<OrderParam> orderParam) {
        ReqLog reqLog = new ReqLog();
        reqLog.setSuccess(true);
        reqLog.setFromBy(orderParam.stream().map(OrderParam::getCustomer).collect(Collectors.joining(",")));
        reqLog.setReqBody(JSON.toJSONString(orderParam));
        reqLog.setType("createOrder");
        try {
            createOrderBatchTx(orderParam);
        }catch (Exception e){
            reqLog.setSuccess(false);
            reqLog.setRepBody(e.getMessage());
        }finally {
            reqLogService.saveReqLog(reqLog);
        }
    }

}
