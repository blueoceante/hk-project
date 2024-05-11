package com.example.hkproject.controller;

import com.example.hkproject.dto.PlaceOrderReq;
import com.example.hkproject.dto.TakeOrderReq;
import com.example.hkproject.entity.GeoPoint;
import com.example.hkproject.thirdParty.GoogleMapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerIntegrationTest {
    @MockBean
    private GoogleMapService googleMapsService;

    @Autowired
    private MockMvc mockMvc;

    private static final String urlPrefix = "/orders";

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) { // 用于配置内存数据库的数据库实例，避免跟其他测试用例的数据库实例冲突
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:hkproject2;MODE=MySQL");
    }

    // 集成测试placeOrder流程
    @Test
    void testPlaceOrder() throws Exception {
        this.performPlaceOrder()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10)) // 数据返回的订单ID是10
                .andExpect(jsonPath("$.distance").value(1000))
                .andExpect(jsonPath("$.status").value("UNASSIGNED"));

    }

    // 集成测试获取订单列表流程
    @Test
    void testGetOrders() throws Exception {
        this.performGetOrders()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(8))
                .andExpect(jsonPath("$[1].id").value(9));
    }

    // 集成测试接单流程
    @Test
    void testTakeOrder() throws Exception {
        this.performTakeOrder(9)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    private ResultActions performPlaceOrder() throws Exception {
        PlaceOrderReq mockReq = new PlaceOrderReq();
        mockReq.setOrigin(new String[]{"39.996794", "116.48105"});
        mockReq.setDestination(new String[]{"20.356786", "100.35105"});
        GeoPoint origin = GeoPoint.parse(mockReq.getOrigin());
        GeoPoint destination = GeoPoint.parse(mockReq.getDestination());
        int distance = 1000; // mock的距离，只mock google map api
        when(googleMapsService.getDistanceMeter(origin, destination)).thenReturn(distance);
        return mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockReq)));

    }

    private ResultActions performGetOrders() throws Exception {
        return mockMvc.perform(get(urlPrefix+ "?page=1&limit=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new ArrayList<>())));
    }

    private ResultActions performTakeOrder(long orderId) throws Exception {
        return mockMvc.perform(patch(urlPrefix+ "/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new TakeOrderReq())));
    }
}
