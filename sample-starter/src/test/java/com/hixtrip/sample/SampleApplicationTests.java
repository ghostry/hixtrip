package com.hixtrip.sample;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SampleApplicationTests {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testInventory() {
        String skuId = "dkdkdkdk22222";
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
}