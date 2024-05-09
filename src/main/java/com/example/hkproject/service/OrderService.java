package com.example.hkproject.service;

import com.example.hkproject.po.OrderTab;

public interface OrderService {
    int insertOrder(OrderTab order);
    String takeOrder(long orderId);
}
