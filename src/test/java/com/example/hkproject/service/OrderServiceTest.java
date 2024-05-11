package com.example.hkproject.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hkproject.constant.OrderStatus;
import com.example.hkproject.constant.TakeOrderRetStatus;
import com.example.hkproject.dao.OrderMapper;
import com.example.hkproject.dto.OrderResp;
import com.example.hkproject.dto.PlaceOrderReq;
import com.example.hkproject.entity.GeoPoint;
import com.example.hkproject.po.OrderTab;
import com.example.hkproject.thirdParty.GoogleMapService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @Mock
    private OrderMapper orderMapper;

    @Mock
    private GoogleMapService googleMapsService;

    @InjectMocks
    private OrderServiceImpl orderService;

    // 测试placeOrder方法，返回数据库插入的订单信息
    @Test
    public void placeOrder_ShouldCreateOrder() throws Exception {
        // Arrange
        String[] origin = new String[]{"39.996794", "116.48105"};
        String[] destination = new String[]{"20.356786", "100.35105"};
        PlaceOrderReq mockReq = new PlaceOrderReq();
        mockReq.setOrigin(origin);
        mockReq.setDestination(destination);
        GeoPoint originPoint = GeoPoint.parse(origin);
        GeoPoint destinationPoint = GeoPoint.parse(destination);
        int distance = 1000; // 假设的距离
        when(googleMapsService.getDistanceMeter(originPoint, destinationPoint)).thenReturn(distance);
        when(orderMapper.insert(any(OrderTab.class))).thenReturn(5);

        // Act
        OrderTab result = orderService.placeOrder(mockReq);

        // Assert
        assertNotNull(result);
        assertEquals((Integer) OrderStatus.UNASSIGNED.getValue(), result.getOrderStatus());
        assertEquals((Integer) distance, result.getDistance());
    }

    @Test
    public void takeOrder_ShouldUpdateOrderStatus() throws InterruptedException {
        // Arrange
        long orderId = 1L;
        OrderTab existingOrder = new OrderTab();
        existingOrder.setOrderStatus(OrderStatus.UNASSIGNED.getValue());
        when(orderMapper.selectById(orderId)).thenReturn(existingOrder);

        // Act
        TakeOrderRetStatus status = orderService.takeOrder(orderId);

        // Assert
        assertEquals(TakeOrderRetStatus.SUCCESS, status);
    }

    @Test
    public void takeOrder_ShouldReturnOrderNotExist() throws InterruptedException {
        // Arrange
        long orderId = 1L;
        when(orderMapper.selectById(orderId)).thenReturn(null);

        // Act
        TakeOrderRetStatus status = orderService.takeOrder(orderId);

        // Assert
        assertEquals(TakeOrderRetStatus.ORDERNOTEXIST, status);
    }

    @Test
    public void getOrders_ShouldReturnOrderList() {
        // Arrange
        int pageNum = 1;
        int limit = 10;
        List<OrderTab> orderTabs = new ArrayList<>();
        orderTabs.add(new OrderTab());
        orderTabs.add(new OrderTab());
        Page<OrderTab> page = new Page<>(pageNum, limit);
        page.setRecords(orderTabs);
        when(orderMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(page);

        // Act
        List<OrderResp> result = orderService.getOrders(pageNum, limit);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

}
