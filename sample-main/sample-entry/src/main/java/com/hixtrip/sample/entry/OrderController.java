package com.hixtrip.sample.entry;

import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这是你要实现的
 */
@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderDomainService orderDomainService;

    /**
     * 这是你要实现的接口
     *
     * @param commandOderCreateDTO 入参对象
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/create")
    public String order(@RequestBody CommandOderCreateDTO commandOderCreateDTO) {
        Order order = new Order();
        order.setSellerId("1111111");
        order.setUserId(commandOderCreateDTO.getUserId());
        order.setSkuId(commandOderCreateDTO.getSkuId());
        order.setAmount(commandOderCreateDTO.getAmount());
        try {
            orderDomainService.createOrder(order);
        } catch (Exception e) {
            return "库存不足，下单出错";
        }
        return "下单成功";
    }

    /**
     * 这是模拟创建订单后，支付结果的回调通知
     * 【中、高级要求】需要使用策略模式处理至少三种场景：支付成功、支付失败、重复支付(自行设计回调报文进行重复判定)
     *
     * @param commandPayDTO 入参对象
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/pay/callback")
    public String payCallback(@RequestBody CommandPayDTO commandPayDTO) {
        log.info("收到支付回调请求 {}", commandPayDTO);
        CommandPay commandPay = new CommandPay();
        BeanUtils.copyProperties(commandPayDTO, commandPay);
        switch (commandPayDTO.getPayStatus()) {
            case "1" -> orderDomainService.orderPaySuccess(commandPay);
            case "2" -> orderDomainService.orderPayFail(commandPay);
            default -> throw new IllegalStateException("Unexpected value: " + commandPayDTO.getPayStatus());
        }
        return "success";
    }

}