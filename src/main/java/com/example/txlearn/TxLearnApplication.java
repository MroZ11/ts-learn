package com.example.txlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync  //开启异步线程配置
public class TxLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxLearnApplication.class, args);
    }

}
