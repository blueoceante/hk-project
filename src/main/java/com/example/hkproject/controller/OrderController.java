package com.example.hkproject.controller;

import com.example.hkproject.dto.PlaceOrderReq;
import com.example.hkproject.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody @Valid PlaceOrderReq req) {
//        OrderTab order = new OrderTab();
//        order.setOrderStatus(1);
//        order.setDistance(1000);
//        order.setOriginLongitude(new BigDecimal("116.48105"));
//        order.setOriginLatitude(new BigDecimal("39.996794"));
//        order.setDestLongitude(new BigDecimal("116.514203"));
//        order.setDestLatitude(new BigDecimal("39.905409"));
//        order.setCtime((int) (System.currentTimeMillis() / 1000));
//        order.setMtime((int) (System.currentTimeMillis() / 1000));
//        order.setIsDel(0);
//        orderService.insertOrder(order);
        return "Order Placed";
    }

    @PatchMapping("/orders/{id}")
    public String takeOrder(@PathVariable long id) {
        // 输出订单ID
        String takeStatus = orderService.takeOrder(id);
        return takeStatus;
    }

    @GetMapping("/orders?page=:page&limit=:limit")
    public String getOrders(@RequestParam int page, @RequestParam int limit) {
        // 输出页码和限制
        return "Orders: Page " + page + ", Limit " + limit;
    }
}