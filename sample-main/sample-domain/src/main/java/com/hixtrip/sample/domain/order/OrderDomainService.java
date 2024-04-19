package com.hixtrip.sample.domain.order;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单领域服务
 */
@Component
public class OrderDomainService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * 创建待付款订单
     */
    public void createOrder(Order order) throws Exception {
        //需要你在infra实现, 自行定义出入参
        // 预占库存
        if (!inventoryRepository.withHolding(order.getSkuId(), Long.valueOf(order.getAmount()))) {
            throw new Exception("预占库存失败");
        }
        orderRepository.createOrder(order);
    }

    /**
     * 待付款订单支付成功
     */
    public void orderPaySuccess(CommandPay commandPay) {
        //需要你在infra实现, 自行定义出入参
        if (!"1".equals(commandPay.getPayStatus())) {
            // 非成功状态，不予处理
            return;
        }
        // 查询订单信息
        Order order = orderRepository.getOrder(commandPay.getOrderId());
        // 订单已经是成功状态，直接返回成功
        if ("1".equals(order.getPayStatus())) {
            return;
        }
        // 支付成功，占用库存
        inventoryRepository.occupied(order.getSkuId(), Long.valueOf(order.getAmount()));
        orderRepository.orderPaySuccess(commandPay);
    }

    /**
     * 待付款订单支付失败
     */
    public void orderPayFail(CommandPay commandPay) {
        //需要你在infra实现, 自行定义出入参
        orderRepository.orderPayFail(commandPay);
    }
}