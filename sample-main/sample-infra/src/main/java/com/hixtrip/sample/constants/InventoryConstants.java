package com.hixtrip.sample.constants;

public interface InventoryConstants {

    /**
     * 可售库存
     */
    String INVENTORY_SELLABLE_QUANTITY = "sellable_quantity";

    /**
     * 预占库存
     */
    String INVENTORY_WITHHOLDING_QUANTITY = "withholding_quantity";

    /**
     * 占用库存
     */
    String INVENTORY_OCCUPIED_QUANTITY = "occupied_quantity";

    /**
     * redis key 前缀
     */
    String INVENTORY_KEY = "inventory:";

}