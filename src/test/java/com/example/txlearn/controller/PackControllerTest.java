package com.example.txlearn.controller;

import com.alibaba.fastjson2.JSON;
import com.example.txlearn.dto.OrderParam;
import com.example.txlearn.dto.PackParam;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PackControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    public static OrderParam wrongOrder = new OrderParam(null,new BigDecimal(0.5),10,"JACK");
    public static OrderParam correctOrder1 =new OrderParam("Bike",new BigDecimal(125),1,"JACK");
    public static OrderParam correctOrder2 = new OrderParam("UAV",new BigDecimal(500),1,"JACK");


    @Test
    void addTxSuccess() throws Exception {
        PackParam param = new PackParam();
        param.setAddress("Area ST No.254");
        param.setCustomer("JACK");
        param.setOrderList(Arrays.asList(correctOrder1,correctOrder2));

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/pack/addTx")
                        .content(JSON.toJSONString(param))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);

    }

    @Test
    void addTxFail() throws Exception {
        PackParam param = new PackParam();
        param.setAddress("Area ST No.254");
        param.setCustomer("JACK");
        param.setOrderList(Arrays.asList(correctOrder1,wrongOrder,correctOrder2));

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/pack/addTx")
                        .content(JSON.toJSONString(param))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }


    @Test
    void addTx2Success() throws Exception {
        PackParam param = new PackParam();
        param.setAddress("Area ST No.254");
        param.setCustomer("JACK");
        param.setOrderList(Arrays.asList(correctOrder1,correctOrder2));

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/pack/addTx2")
                        .content(JSON.toJSONString(param))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);

    }

    @Test
    void addTx2Fail() throws Exception {
        PackParam param = new PackParam();
        param.setAddress("Area ST No.254");
        param.setCustomer("JACK");
        param.setOrderList(Arrays.asList(correctOrder1,wrongOrder,correctOrder2));

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/pack/addTx2")
                        .content(JSON.toJSONString(param))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }


    @Test
    void addTx3Success() throws Exception {
        PackParam param = new PackParam();
        param.setAddress("Area ST No.254");
        param.setCustomer("JACK");
        param.setOrderList(Arrays.asList(correctOrder1,correctOrder2));

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/pack/addTx3")
                        .content(JSON.toJSONString(param))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);

    }

    @Test
    void addTx3Fail() throws Exception {
        PackParam param = new PackParam();
        param.setAddress("Area ST No.254");
        param.setCustomer("JACK");
        param.setOrderList(Arrays.asList(correctOrder1,wrongOrder,correctOrder2));

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/pack/addTx3")
                        .content(JSON.toJSONString(param))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }




}
