package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import com.hixtrip.sample.infra.db.convertor.OrderDOConvertor;
import com.hixtrip.sample.infra.db.dataobject.OrderDO;
import com.hixtrip.sample.infra.db.mapper.OrderMapper;
import com.hixtrip.sample.util.Douyin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderRepositoryImpl implements OrderRepository {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 创建订单
     *
     * @param order
     */
    @Override
    public void createOrder(Order order) {
        // 查询SKU价格
        BigDecimal skuMoney = Douyin.getSkuMoney(order.getSkuId());
        order.setMoney(skuMoney);
        // 下单入库
        orderMapper.insert(OrderDOConvertor.INSTANCE.domainToDo(order));
    }

    /**
     * 支付成功
     *
     * @param commandPay
     */
    @Override
    public void orderPaySuccess(CommandPay commandPay) {
        OrderDO orderDO = orderMapper.selectById(commandPay.getOrderId());
        orderDO.setPayStatus("1");
        // 修改订单状态
        orderMapper.updateById(orderDO);
    }

    /**
     * 支付失败
     *
     * @param commandPay
     */
    @Override
    public void orderPayFail(CommandPay commandPay) {
        // 失败不做处理，等待再次支付，实际业务可能更复杂，需要处理
    }

    @Override
    public Order getOrder(String orderId) {
        return OrderDOConvertor.INSTANCE.doToDomain(orderMapper.selectById(orderId));
    }
}