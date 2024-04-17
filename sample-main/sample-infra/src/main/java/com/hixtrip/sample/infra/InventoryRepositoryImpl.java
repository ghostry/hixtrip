package com.hixtrip.sample.infra;

import com.hixtrip.sample.constants.InventoryConstants;
import com.hixtrip.sample.domain.inventory.model.Inventory;
import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * infra层是domain定义的接口具体的实现
 * 根据题目，限制在spring，redis内实现功能
 * 利用redis increment的原子性实现在高并发情况下的限购
 * 实际项目建议在用户点击下单时使用分布式消息队列。
 */
@Component
public class InventoryRepositoryImpl implements InventoryRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据skuId获得库存情况
     *
     * @param skuId
     * @return
     */
    @Override
    public Inventory getInventory(String skuId) {
        String key = InventoryConstants.INVENTORY_KEY + skuId;
        try {
            Long sellableQuantity = Long.parseLong(String.valueOf(redisTemplate.opsForHash().get(key, InventoryConstants.INVENTORY_SELLABLE_QUANTITY)));
            Long withholdingQuantity = Long.parseLong(String.valueOf(redisTemplate.opsForHash().get(key, InventoryConstants.INVENTORY_WITHHOLDING_QUANTITY)));
            Long occupiedQuantity = Long.parseLong(String.valueOf(redisTemplate.opsForHash().get(key, InventoryConstants.INVENTORY_OCCUPIED_QUANTITY)));
            return new Inventory(skuId, sellableQuantity, withholdingQuantity, occupiedQuantity);
        } catch (Exception e) {
            return new Inventory(skuId);
        }
    }

    /**
     * 预占库存
     *
     * @param skuId
     * @param amount
     * @return
     */
    @Override
    public Boolean withHolding(String skuId, Long amount) {
        String key = InventoryConstants.INVENTORY_KEY + skuId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key)) && Long.parseLong(String.valueOf(redisTemplate.opsForHash().get(key, InventoryConstants.INVENTORY_SELLABLE_QUANTITY))) >= amount) {
            if (redisTemplate.opsForHash().increment(key, InventoryConstants.INVENTORY_SELLABLE_QUANTITY, -amount) >= 0) {
                redisTemplate.opsForHash().increment(key, InventoryConstants.INVENTORY_WITHHOLDING_QUANTITY, amount);
                return true;
            } else {
                //存在超卖，预占库存失败
                redisTemplate.opsForHash().increment(key, InventoryConstants.INVENTORY_SELLABLE_QUANTITY, amount);
            }
        }
        return false;
    }

    /**
     * 占用库存
     *
     * @param skuId
     * @param amount
     * @return
     */
    @Override
    public Boolean occupied(String skuId, Long amount) {
        String key = InventoryConstants.INVENTORY_KEY + skuId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key)) && Long.parseLong(String.valueOf(redisTemplate.opsForHash().get(key, InventoryConstants.INVENTORY_WITHHOLDING_QUANTITY))) >= amount) {
            if (redisTemplate.opsForHash().increment(key, InventoryConstants.INVENTORY_WITHHOLDING_QUANTITY, -amount) >= 0) {
                redisTemplate.opsForHash().increment(key, InventoryConstants.INVENTORY_OCCUPIED_QUANTITY, amount);
                return true;
            } else {
                //占用异常
                redisTemplate.opsForHash().increment(key, InventoryConstants.INVENTORY_WITHHOLDING_QUANTITY, amount);
            }
        }
        return false;
    }

    /**
     * 修改可售库存
     *
     * @param skuId
     * @param sellableQuantity
     * @return
     */
    @Override
    public Boolean sellable(String skuId, Long sellableQuantity) {
        String key = InventoryConstants.INVENTORY_KEY + skuId;
        redisTemplate.opsForHash().put(key, InventoryConstants.INVENTORY_SELLABLE_QUANTITY, sellableQuantity);
        return true;
    }

}