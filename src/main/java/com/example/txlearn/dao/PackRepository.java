package com.example.txlearn.dao;

import com.example.txlearn.entity.Pack;
import com.example.txlearn.entity.ReqLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackRepository extends JpaRepository<Pack, Long> {
}
