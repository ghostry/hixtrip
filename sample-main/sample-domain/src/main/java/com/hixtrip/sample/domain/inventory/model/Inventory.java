package com.hixtrip.sample.domain.inventory.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inventory {

    private String skuId;
    // 可售库存
    private Long sellableQuantity;
    // 预占库存
    private Long withholdingQuantity;
    // 占用库存
    private Long occupiedQuantity;

    public Inventory() {
    }

    public Inventory(String skuId) {
        this.setSkuId(skuId);
        this.setSellableQuantity(0L);
        this.setWithholdingQuantity(0L);
        this.setOccupiedQuantity(0L);
    }

    public Inventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        this.setSkuId(skuId);
        this.setSellableQuantity(sellableQuantity);
        this.setWithholdingQuantity(withholdingQuantity);
        this.setOccupiedQuantity(occupiedQuantity);
    }
}