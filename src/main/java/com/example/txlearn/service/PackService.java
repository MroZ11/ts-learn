package com.example.txlearn.service;


import com.alibaba.fastjson2.JSON;
import com.example.txlearn.dao.PackRepository;
import com.example.txlearn.dto.OrderParam;
import com.example.txlearn.dto.PackParam;
import com.example.txlearn.entity.Order;
import com.example.txlearn.entity.Pack;
import com.example.txlearn.entity.ReqLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackService {


    @Autowired
    private PackRepository packRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReqLogService reqLogService;

    @Autowired
    private TransactionTemplate txTemplate;


    @Transactional(rollbackFor = Exception.class)
    public void createPackTx(PackParam packParam) {

        ReqLog reqLog = new ReqLog();
        reqLog.setSuccess(true);
        reqLog.setFromBy(packParam.getCustomer());
        reqLog.setReqBody(JSON.toJSONString(packParam));
        reqLog.setType("createPack");

        try {
            Pack pack = new Pack();
            pack.setCustomer(packParam.getCustomer());
            pack.setAddress(packParam.getAddress());
            final Pack saved = packRepository.save(pack);

            final List<Order> orderBatchTx = orderService.createOrderBatchTx(packParam.getOrderList());

            saved.setOrderList(orderBatchTx);
            packRepository.saveAndFlush(saved);
        } catch (Exception e) {
            reqLog.setSuccess(false);
            reqLog.setRepBody(e.getMessage());
        } finally {
            //即便try catch了 也无法保存，因为默认事务会传播
            // Transaction silently rolled back because it has been marked as rollback-only
            reqLogService.saveReqLog(reqLog);
        }

    }




    @Transactional(rollbackFor = Exception.class)
    public void createPackTx2(PackParam packParam) {

        ReqLog reqLog = new ReqLog();
        reqLog.setSuccess(true);
        reqLog.setFromBy(packParam.getCustomer());
        reqLog.setReqBody(JSON.toJSONString(packParam));
        reqLog.setType("createPack");

        try {
            Pack pack = new Pack();
            pack.setCustomer(packParam.getCustomer());
            pack.setAddress(packParam.getAddress());
            final Pack saved = packRepository.save(pack);

            final List<Order> orderBatchTx = orderService.createOrderBatchTx(packParam.getOrderList());

            saved.setOrderList(orderBatchTx);
            packRepository.saveAndFlush(saved);
        } catch (Exception e) {
            reqLog.setSuccess(false);
            reqLog.setRepBody(e.getMessage());
            //这里明确抛出异常 不然全局异常会捕捉为事务回滚
            //"Transaction silently rolled back because it has been marked as rollback-only"
            throw new RuntimeException(reqLog.getRepBody());
        } finally {
            //标记为不支持事务 这样即便回滚日志也能保存
            //还有一种方式是将 saveReqLog异步执行 使用 @EnableAsync开启异步执行 并@Async标记方法
            //  或者 需要配合@Transactional(propagation = Propagation.REQUIRES_NEW)
            // var ts = new TransactionSynchronization(){
            //       @Override
            //       public void afterCompletion(int status) {
            //           //事务完成后置处理
            //      }
            // }
            // TransactionSynchronizationManager.registerSynchronization(ts);
            //
            reqLogService.saveReqLogNoTx(reqLog);
        }

    }


    public void createPackTx3(PackParam packParam) {
        //使用编程式事务 可以更精准的控制事务粒度
        //https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch11s06.html
        //TransactionDefinition
        ReqLog reqLog = new ReqLog();
        reqLog.setSuccess(true);
        reqLog.setFromBy(packParam.getCustomer());
        reqLog.setReqBody(JSON.toJSONString(packParam));
        reqLog.setType("createPack");


        //每一次 execute是一个独立的事务
        txTemplate.executeWithoutResult((TransactionStatus status) -> {
            try {
                Pack pack = new Pack();
                pack.setCustomer(packParam.getCustomer());
                pack.setAddress(packParam.getAddress());
                final Pack saved = packRepository.save(pack);

                final List<Order> orderBatchTx = orderService.createOrderBatchTx(packParam.getOrderList());

                saved.setOrderList(orderBatchTx);
                packRepository.saveAndFlush(saved);

            }catch (Exception e){
                status.setRollbackOnly();
                reqLog.setSuccess(false);
                reqLog.setRepBody(e.getMessage());

            }finally {
                //PROPAGATION_REQUIRED沿用之前事务 异常出现时会导致saveReqLog(reqLog) 也被回滚
                //txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

                //开启新事务 之前的事务不会影响 saveReqLog(reqLog)
                txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                txTemplate.executeWithoutResult(transactionStatus -> {
                    reqLogService.saveReqLog(reqLog);
                });
            }
        });







    }


}
