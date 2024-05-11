package com.example.hkproject.controller;

import com.example.hkproject.constant.TakeOrderRetStatus;
import com.example.hkproject.dto.OrderResp;
import com.example.hkproject.dto.PlaceOrderReq;
import com.example.hkproject.dto.TakeOrderReq;
import com.example.hkproject.dto.TakeOrderResp;
import com.example.hkproject.exception.TakeOrderException;
import com.example.hkproject.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResp placeOrder(@RequestBody @Valid PlaceOrderReq req) throws Exception {
        return orderService.placeOrder(req);
    }

    @PatchMapping("/{id}")
    public TakeOrderResp takeOrder(@PathVariable @Min(1) long id, @RequestBody @Valid TakeOrderReq req) throws Exception {
        // 输出订单ID
        TakeOrderRetStatus retStatus = orderService.takeOrder(id);
        if (retStatus == TakeOrderRetStatus.SUCCESS) {
            return new TakeOrderResp(retStatus.getDescription());
        } else {
            throw new TakeOrderException(retStatus.getDescription());
        }
    }

    @GetMapping
    public List<OrderResp> getOrders(@RequestParam @Min(1) int page, @RequestParam @Min(1) int limit) {
        return orderService.getOrders(page, limit);
    }
}