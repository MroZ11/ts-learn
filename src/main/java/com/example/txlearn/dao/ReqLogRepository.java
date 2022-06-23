package com.example.txlearn.dao;

import com.example.txlearn.entity.ReqLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReqLogRepository extends JpaRepository<ReqLog, Long> {
}
