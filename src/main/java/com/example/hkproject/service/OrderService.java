package com.example.hkproject.service;

import com.example.hkproject.constant.TakeOrderRetStatus;
import com.example.hkproject.dto.OrderResp;
import com.example.hkproject.dto.PlaceOrderReq;
import com.example.hkproject.po.OrderTab;

import java.util.List;

public interface OrderService {
    OrderTab placeOrder(PlaceOrderReq req);
    TakeOrderRetStatus takeOrder(long orderId);
    List<OrderResp> getOrders(int page, int limit);
}
