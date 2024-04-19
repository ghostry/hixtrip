package com.hixtrip.sample.domain.order.repository;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.model.CommandPay;

/**
 *
 */
public interface OrderRepository {
    void createOrder(Order order);

    void orderPaySuccess(CommandPay commandPay);

    void orderPayFail(CommandPay commandPay);

    Order getOrder(String orderId);
}