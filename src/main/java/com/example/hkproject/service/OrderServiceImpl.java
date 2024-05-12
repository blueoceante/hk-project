package com.example.hkproject.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hkproject.constant.OrderStatus;
import com.example.hkproject.constant.TakeOrderRetStatus;
import com.example.hkproject.dao.OrderMapper;
import com.example.hkproject.dto.OrderResp;
import com.example.hkproject.dto.PlaceOrderReq;
import com.example.hkproject.entity.GeoPoint;
import com.example.hkproject.po.OrderTab;
import com.example.hkproject.thirdParty.GoogleMapService;
import com.example.hkproject.util.PoToDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final GoogleMapService googleMapsService;
    // 订单ID锁，解决并发问题，不使用数据库乐观锁，加速订单处理，如果是分布式环境，可以使用redis分布式锁
    private final ConcurrentHashMap<Long, ReentrantLock> orderLocks = new ConcurrentHashMap<>();

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, GoogleMapService googleMapsService) {
        this.orderMapper = orderMapper;
        this.googleMapsService = googleMapsService;
    }

    @Override
    public OrderResp placeOrder(PlaceOrderReq req) throws Exception {
        // 调用Google Maps API计算距离
        GeoPoint origin = GeoPoint.parse(req.getOrigin());
        GeoPoint destination = GeoPoint.parse(req.getDestination());
        int distance = googleMapsService.getDistanceMeter(origin, destination);
        // 创建订单
        OrderTab orderTab = new OrderTab();
        orderTab.setOrderStatus(OrderStatus.UNASSIGNED.getValue());
        orderTab.setDistance(distance);
        orderTab.setOriginLatitude(origin.getLatitude());
        orderTab.setOriginLongitude(origin.getLongitude());
        orderTab.setDestLatitude(destination.getLatitude());
        orderTab.setDestLongitude(destination.getLongitude());
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        orderTab.setCtime(nowTime);
        orderTab.setMtime(nowTime);
        orderTab.setIsDel(0);
        // 插入订单
        orderMapper.insert(orderTab);
        return PoToDto.orderTabToOrderResp(orderTab);
    }

    @Override
    public TakeOrderRetStatus takeOrder(long orderId) {
        ReentrantLock lock = orderLocks.computeIfAbsent(orderId, k -> new ReentrantLock());
        boolean isLocked = false;
        try {
            // 尝试在5秒内获取锁，如果无法获取，就放弃
            isLocked = lock.tryLock(5, TimeUnit.SECONDS);
            if (!isLocked) {
                // 如果无法获得锁，意味着订单正在处理中或者获取锁超时
                return TakeOrderRetStatus.TAKEINGBYUSER;
            }
            // 检查订单状态
            OrderTab orderTab = orderMapper.selectById(orderId);
            if (orderTab == null) {
                return TakeOrderRetStatus.ORDERNOTEXIST;
            }
            if (orderTab.getOrderStatus() == OrderStatus.TAKEN.getValue()) {
                return TakeOrderRetStatus.PROCESSED;
            }
            // 更新订单状态等
            UpdateWrapper<OrderTab> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", orderId) // 设置where条件
                    .set("order_status", OrderStatus.TAKEN.getValue()); // 设置要更新的字段
            orderMapper.update(updateWrapper);
            return TakeOrderRetStatus.SUCCESS;
        } catch (InterruptedException e) {
            // 处理异常
            log.error("takeOrder error", e);
            return TakeOrderRetStatus.ERROR;
        } finally {
            if (isLocked && lock.isLocked()) {
                // 释放锁
                lock.unlock();
                // 移除锁，以避免内存泄漏
                orderLocks.remove(orderId);
            }
        }
    }

    @Override
    public List<OrderResp> getOrders(int pageNum, int limit) {
        // 创建Page对象
        Page<OrderTab> page = new Page<>(pageNum, limit);
        // 创建QueryWrapper对象
        QueryWrapper<OrderTab> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        page = orderMapper.selectPage(page, wrapper);
        // 获取查询结果
        List<OrderTab> orderTabs = page.getRecords();
        if (orderTabs == null || orderTabs.isEmpty()) {
            return new ArrayList<>();
        }
        // 将PO转换为DTO
        List<OrderResp> orderResps = new ArrayList<>();
        for (OrderTab orderTab : orderTabs) {
            OrderResp orderResp = PoToDto.orderTabToOrderResp(orderTab);
            if (orderResp != null) {
                orderResps.add(orderResp);
            }
        }
        return orderResps;
    }
}
