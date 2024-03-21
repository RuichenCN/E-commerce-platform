package org.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import org.skillup.domain.promotionCache.PromotionCacheDomain;
import org.skillup.domain.promotionCache.PromotionCacheRepo;
import org.skillup.domain.stock.StockDomain;
import org.skillup.domain.stock.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Repository
public class RedisRepo implements StockRepository, PromotionCacheRepo {
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    DefaultRedisScript<Long> redisLockStockScript;
    @Autowired
    DefaultRedisScript<Long> redisRevertStockScript;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean lockAvailableStock(StockDomain stockDomain) {
        // 0 Lua script to ACID below operations
        // 1 select from available stock = ?
        // 2 if available stock > 0 then update available stock = available stock - 1
        try {
            Long stock = redisTemplate.execute(redisLockStockScript,
                    Collections.singletonList(
                            StockDomain.createStockKey(stockDomain.getPromotionId())
                    ));
            if (stock >= 0) {
                return true;
            } else {
                // -1 means sold out, -2 promotion doesn't exist
                return false;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public boolean revertAvailableStock(StockDomain stockDomain) {
        // 0 Lua script to ACID below operations
        // 1 select from available stock = ?
        // 2 if available stock > 0 then update available stock = available stock - 1
        try {
            Long stock = redisTemplate.execute(redisRevertStockScript,
                    Collections.singletonList(
                            StockDomain.createStockKey(stockDomain.getPromotionId())
                    ));
            if (stock > 0) {
                return true;
            } else {
                // -1 means sold out, -2 promotion doesn't exist
                return false;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public Long getPromotionAvailableStock(String promotionId) {
        // create stock key
        String key = StockDomain.createStockKey(promotionId);
        return JSON.parseObject(get(key), Long.class);
    }

    @Override
    public void setPromotionAvailableStock(String promotionId, Long availableStock) {
        // create stock key
        String key = StockDomain.createStockKey(promotionId);
        set(key, availableStock);
    }

    @Override
    public PromotionCacheDomain getPromotionById(String id) {
        return JSON.parseObject(get(id), PromotionCacheDomain.class);
    }

    @Override
    public void setPromotion(PromotionCacheDomain cacheDomain) {
        set(cacheDomain.getPromotionId(), cacheDomain);
    }
}
