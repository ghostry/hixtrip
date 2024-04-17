package com.hixtrip.sample.domain.inventory;

import com.hixtrip.sample.domain.inventory.model.Inventory;
import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 库存领域服务
 * 库存设计，忽略仓库、库存品、计量单位等业务
 */
@Component
public class InventoryDomainService {
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * 获取sku当前库存
     *
     * @param skuId
     */
    public Integer getInventory(String skuId) {
        // 需要你在infra实现，只需要实现缓存操作, 返回的领域对象自行定义
        Inventory inventory = inventoryRepository.getInventory(skuId);
        return Math.toIntExact(inventory.getSellableQuantity());
    }

    /**
     * 修改库存
     *
     * @param skuId
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return Boolean
     */
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        // 需要你在infra实现，只需要实现缓存操作。
        if (0 != withholdingQuantity) {
            // 预占库存
            return inventoryRepository.withHolding(skuId, withholdingQuantity);
        } else if (0 != occupiedQuantity) {
            // 占用库存
            return inventoryRepository.occupied(skuId, occupiedQuantity);
        } else if (0 < sellableQuantity) {
            // 修改可售库存、
            return inventoryRepository.sellable(skuId, sellableQuantity);
        }
        return false;
    }
}