package com.example.txlearn.service;

import com.example.txlearn.dao.ReqLogRepository;
import com.example.txlearn.entity.ReqLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReqLogService {

    @Autowired
    private ReqLogRepository reqLogRpository;

    public void saveReqLog(ReqLog reqLog){
        reqLogRpository.saveAndFlush(reqLog);
    }


    //@Async 异步也可以
    //@Transactional(propagation = Propagation.REQUIRES_NEW) 创建新事务也可以
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void saveReqLogNoTx(ReqLog reqLog){
        reqLogRpository.saveAndFlush(reqLog);
    }


}
