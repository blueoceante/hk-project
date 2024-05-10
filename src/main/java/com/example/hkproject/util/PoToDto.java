package com.example.hkproject.util;

import com.example.hkproject.constant.OrderStatus;
import com.example.hkproject.dto.OrderResp;
import com.example.hkproject.po.OrderTab;

public class PoToDto {

    // OrderTab -> OrderResp
    public static OrderResp orderTabToOrderResp(OrderTab orderTab) {
        if (orderTab == null) {
            return null;
        }
        OrderResp orderResp = new OrderResp();
        orderResp.setId(orderTab.getId());
        orderResp.setDistance(orderTab.getDistance());
        orderResp.setStatus(OrderStatus.fromValue(orderTab.getOrderStatus()).getDescription());
        return orderResp;
    }
}
