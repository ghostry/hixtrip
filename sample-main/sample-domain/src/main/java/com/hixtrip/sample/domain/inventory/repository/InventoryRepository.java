package com.hixtrip.sample.domain.inventory.repository;

import com.hixtrip.sample.domain.inventory.model.Inventory;

/**
 *
 */
public interface InventoryRepository {

    Inventory getInventory(String skuId);

    Boolean withHolding(String skuId, Long amount);

    Boolean occupied(String skuId, Long amount);

    Boolean sellable(String skuId, Long sellableQuantity);
}