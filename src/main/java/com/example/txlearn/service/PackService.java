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
            //??????try catch??? ?????????????????????????????????????????????
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
            //???????????????????????? ??????????????????????????????????????????
            //"Transaction silently rolled back because it has been marked as rollback-only"
            throw new RuntimeException(reqLog.getRepBody());
        } finally {
            //???????????????????????? ????????????????????????????????????
            //???????????????????????? saveReqLog???????????? ?????? @EnableAsync?????????????????? ???@Async????????????
            //  ?????? ????????????@Transactional(propagation = Propagation.REQUIRES_NEW)
            // var ts = new TransactionSynchronization(){
            //       @Override
            //       public void afterCompletion(int status) {
            //           //????????????????????????
            //      }
            // }
            // TransactionSynchronizationManager.registerSynchronization(ts);
            //
            reqLogService.saveReqLogNoTx(reqLog);
        }

    }


    public void createPackTx3(PackParam packParam) {
        //????????????????????? ????????????????????????????????????
        //https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch11s06.html
        //TransactionDefinition
        ReqLog reqLog = new ReqLog();
        reqLog.setSuccess(true);
        reqLog.setFromBy(packParam.getCustomer());
        reqLog.setReqBody(JSON.toJSONString(packParam));
        reqLog.setType("createPack");


        //????????? execute????????????????????????
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
                //PROPAGATION_REQUIRED?????????????????? ????????????????????????saveReqLog(reqLog) ????????????
                //txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

                //??????????????? ??????????????????????????? saveReqLog(reqLog)
                txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                txTemplate.executeWithoutResult(transactionStatus -> {
                    reqLogService.saveReqLog(reqLog);
                });
            }
        });







    }


}
