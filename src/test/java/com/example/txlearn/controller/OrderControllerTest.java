package com.example.txlearn.controller;

import com.alibaba.fastjson2.JSON;
import com.example.txlearn.dto.OrderParam;
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
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    public static OrderParam wrongOrder = new OrderParam(null,new BigDecimal(0.5),10,"Tom Cat");
    public static OrderParam correctOrder1 =new OrderParam("Bike",new BigDecimal(125),1,"Terry Young");
    public static OrderParam correctOrder2 = new OrderParam("UAV",new BigDecimal(500),1,"Lin Take");




    @Test
    void add() throws Exception {
        OrderParam orderParam = correctOrder1;
        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/add")
                        .content(JSON.toJSONString(orderParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);

    }

    @Test
    void addWithError() throws Exception{
        OrderParam orderParam = wrongOrder;
        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/add")
                        .content(JSON.toJSONString(orderParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }

    @Test
    void addBatch() throws Exception{

        OrderParam p1 = correctOrder1;
        OrderParam p2 = correctOrder2;

        List<OrderParam> list = Arrays.asList(p1,p2);

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/addBatch")
                        .content(JSON.toJSONString(list))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }



    @Test
    void addBatchWithErrorNoTx() throws Exception{
        //No tx save will not roll back
        OrderParam p1 = correctOrder1;//param correct will save to db
        OrderParam p2 = wrongOrder;//param wrong,can't save
        OrderParam p3 = correctOrder2;//param correct,but error throw,cycle interrupt，can't save
        List<OrderParam> list = Arrays.asList(p1,p2,p3);

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/addBatch")
                        .content(JSON.toJSONString(list))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }


    @Test
    void addBatchTx() throws Exception{
        //With tx save will  roll back

        OrderParam p1 = correctOrder1;//param correct but roll back when <p2> throws Exception
        OrderParam p2 = wrongOrder;//param wrong,can't save
        OrderParam p3 = correctOrder2;;//param correct,but error throw,cycle interrupt，can't save

        List<OrderParam> list = Arrays.asList(p1,p2,p3);

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/addBatchTx")
                        .content(JSON.toJSONString(list))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }


    @Test
    void addBatchTx2() throws Exception{
        //avoid rollback through  catch exceptions and reduce granularity

        OrderParam p1 = correctOrder1;// still save success
        OrderParam p2 = wrongOrder;//param wrong,can't save
        OrderParam p3 = correctOrder2;;//still save success

        List<OrderParam> list = Arrays.asList(p1,p2,p3);

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/addBatchTx2")
                        .content(JSON.toJSONString(list))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }

    @Test
    void addAndLog() throws Exception{

        OrderParam p1 = wrongOrder;//param wrong,can't save

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/addAndLog")
                        .content(JSON.toJSONString(p1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }

    @Test
    void addBatchAndLogWithTx() throws Exception{

        OrderParam p1 = correctOrder1;// still save success
        OrderParam p2 = wrongOrder;//param wrong,can't save
        OrderParam p3 = correctOrder2;;//still save success

        List<OrderParam> list = Arrays.asList(p1,p2,p3);

        final String rep = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/addBatchAndLogWithTx")
                        .content(JSON.toJSONString(list))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.printf("rep->%s",rep);
    }




}
