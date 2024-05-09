package com.example.hkproject.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.hkproject.constant.OrderStatus;
import com.example.hkproject.dao.OrderMapper;
import com.example.hkproject.po.OrderTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    // 订单ID锁，解决并发问题，不使用数据库乐观锁，加速订单处理，如果是分布式环境，可以使用redis分布式锁
    private final ConcurrentHashMap<Long, ReentrantLock> orderLocks = new ConcurrentHashMap<>();

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public int insertOrder(OrderTab order) {
        return orderMapper.insert(order);
    }

    public String takeOrder(long orderId) {
        ReentrantLock lock = orderLocks.computeIfAbsent(orderId, k -> new ReentrantLock());
        boolean isLocked = false;
        try {
            // 尝试在5秒内获取锁，如果无法获取，就放弃
            isLocked = lock.tryLock(5, TimeUnit.SECONDS);
            if (!isLocked) {
                // 如果无法获得锁，意味着订单正在处理中或者获取锁超时
                return "Order is being processed by another user, please try again later";
            }

            // 检查订单状态
            OrderTab orderTab = orderMapper.selectById(orderId);
            if (orderTab == null) {
                return "Order does not exist";
            }

            if (orderTab.getOrderStatus() == OrderStatus.TAKEN.getValue()) {
                return "Order has already been processed";
            }

            // 更新订单状态等
            UpdateWrapper<OrderTab> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", orderId) // 设置where条件
                    .set("order_status", OrderStatus.TAKEN.getValue()); // 设置要更新的字段
            orderMapper.update(updateWrapper);

            return "Order processed successfully";
        } catch (InterruptedException e) {
            // 处理异常
            return "Something went wrong, please try again";
        } finally {
            if (isLocked) {
                // 释放锁
                lock.unlock();
                // 移除锁，以避免内存泄漏
                orderLocks.remove(orderId);
            }
        }
    }
}
