package com.hixtrip.sample;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SampleApplicationTests {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private OrderDomainService orderDomainService;
    private String skuId = "dkdkdkdk22222";

    @Test
    void contextLoads() {
    }

    @Test
    void testInventory() {
        // 读取库存
        log.info("inventory: {}", inventoryRepository.getInventory(skuId));
        // 修改可售库存
        log.info("sellable: {}", inventoryRepository.sellable(skuId, 100L));
        // 预占库存
        log.info("withHolding: {}", inventoryRepository.withHolding(skuId, 20L));
        // 占用库存
        log.info("occupied: {}", inventoryRepository.occupied(skuId, 2L));
        // 读取库存
        log.info("inventory: {}", inventoryRepository.getInventory(skuId));
    }

    @Test
    void testOrder() {
        // 修改可售库存
        log.info("sellable: {}", inventoryRepository.sellable(skuId, 100L));
        // 下单
        Order order = new Order();
        order.setAmount(2);
        order.setCreateBy("admin");
        order.setSellerId("1111111");
        order.setSkuId(skuId);
        order.setUserId("2222222");
        try {
            orderDomainService.createOrder(order);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 读取库存
        log.info("inventory: {}", inventoryRepository.getInventory(skuId));
        // 支付成功
        CommandPay commandPay = new CommandPay();
        commandPay.setOrderId("1781134822190649346");
        commandPay.setPayStatus("1");
        orderDomainService.orderPaySuccess(commandPay);
        // 读取库存
        log.info("inventory: {}", inventoryRepository.getInventory(skuId));
    }
}