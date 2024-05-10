package com.example.hkproject.controller;

import com.example.hkproject.constant.OrderStatus;
import com.example.hkproject.constant.TakeOrderRetStatus;
import com.example.hkproject.dto.OrderResp;
import com.example.hkproject.dto.PlaceOrderReq;
import com.example.hkproject.dto.TakeOrderReq;
import com.example.hkproject.exception.TakeOrderException;
import com.example.hkproject.po.OrderTab;
import com.example.hkproject.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc // 自动配置MockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService; // 模拟OrderService

    private static final String urlPrefix = "/orders";

    // 测试placeOrder方法，当返回成功状态时，返回订单信息
    @Test
    public void placeOrder_ShouldReturnOrderResp() throws Exception {
        // 准备输入和输出数据
        PlaceOrderReq mockReq = new PlaceOrderReq();
        mockReq.setOrigin(new String[]{"39.996794", "116.48105"});
        mockReq.setDestination(new String[]{"20.356786", "100.35105"});
        // ... 设置mockReq的属性
        OrderTab mockOrderTab = new OrderTab();
        mockOrderTab.setId(1L);
        mockOrderTab.setOrderStatus(OrderStatus.UNASSIGNED.getValue());
        mockOrderTab.setDistance(1000);

        when(orderService.placeOrder(mockReq)).thenReturn(mockOrderTab);
        mockMvc.perform(post(urlPrefix)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance").value(1000));
    }

    // 测试placeOrder方法，当输入数据不合法时，返回异常
    @Test
    public void placeOrder_ShouldReturnValidException() throws Exception {
        // 准备输入和输出数据
        PlaceOrderReq mockReq = new PlaceOrderReq();
        mockReq.setOrigin(new String[]{"39.996794", "22116.48105"});
        mockReq.setDestination(new String[]{"20.356786", "100.35105"});
        OrderTab mockOrderTab = new OrderTab();
        when(orderService.placeOrder(mockReq)).thenReturn(mockOrderTab);
        mockMvc.perform(post(urlPrefix)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockReq)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    // 测试takeOrder方法，当返回成功状态时，返回成功信息
    @Test
    public void takeOrder_ShouldReturnSuccess() throws Exception {
        long orderId = 1L; // 假设的订单ID

        // 当调用orderService.takeOrder时，返回SUCCESS状态
        when(orderService.takeOrder(orderId)).thenReturn(TakeOrderRetStatus.SUCCESS);

        mockMvc.perform(patch(urlPrefix+ "/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new TakeOrderReq())))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SUCCESS")));
    }

    // 测试takeOrder方法，当返回失败状态时，抛出异常
    @Test
    public void takeOrder_ShouldThrowException() throws Exception {
        long orderId = 3L; // 假设的订单ID
        TakeOrderRetStatus failedStatus = TakeOrderRetStatus.PROCESSED; // 假设的失败状态

        // 当调用orderService.takeOrder时，返回失败状态
        when(orderService.takeOrder(orderId)).thenReturn(failedStatus);

        mockMvc.perform(patch(urlPrefix+ "/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new TakeOrderReq())))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TakeOrderException));
    }

    // 测试getOrders方法，当有订单时，返回订单列表
    @Test
    public void getOrders_ShouldReturnOrders() throws Exception {
        OrderResp order1 = new OrderResp();
        order1.setId(5L);
        OrderResp order2 = new OrderResp();
        order2.setId(6L);
        List<OrderResp> mockOrderList = List.of(order1, new OrderResp());
        when(orderService.getOrders(1, 2)).thenReturn(mockOrderList);
        mockMvc.perform(get(urlPrefix+ "?page=1&limit=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ArrayList<>())))
                .andExpect(status().isOk())
                // 检查是不是一个数组
                .andExpect(jsonPath("$", isA(JSONArray.class)))
                // 检查数组里面的元素数量是不是2
                .andExpect(jsonPath("$", hasSize(2)))
                // 检查第一个元素的id是不是5
                .andExpect(jsonPath("$[0].id", is(5)));
    }

    // 测试getOrders方法，当没有订单时，返回空数组
    @Test
    public void getOrders_ShouldReturnEmpty() throws Exception {
        List<OrderResp> mockOrderList = new ArrayList<>();
        when(orderService.getOrders(5, 2)).thenReturn(mockOrderList);
        mockMvc.perform(get(urlPrefix+ "?page=5&limit=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ArrayList<>())))
                .andExpect(status().isOk())
                // 检查是不是一个数组
                .andExpect(jsonPath("$", isA(JSONArray.class)))
                // 检查数组里面的元素数量是不是0
                .andExpect(jsonPath("$", hasSize(0)));
    }

}
