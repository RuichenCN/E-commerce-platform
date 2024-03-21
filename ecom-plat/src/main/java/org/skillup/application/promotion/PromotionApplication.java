package org.skillup.application.promotion;

import org.skillup.application.mapper.PromotionMapper;
import org.skillup.domain.promotion.PromotionDomain;
import org.skillup.domain.promotion.PromotionService;
import org.skillup.domain.promotionCache.PromotionCacheDomain;
import org.skillup.domain.promotionCache.PromotionCacheService;
import org.skillup.domain.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PromotionApplication {
    @Autowired
    StockService stockService;
    @Autowired
    PromotionCacheService promotionCacheService;
    @Autowired
    PromotionService promotionService;
    public PromotionDomain getPromotionById(String id) {
        // cache aside read strategy
        // 1. try to hit cache
        PromotionCacheDomain cacheDomain = promotionCacheService.getPromotionById(id);
        // 2. not hit, read database
        if (Objects.isNull(cacheDomain)) {
            PromotionDomain promotionDomain = promotionService.getPromotionById(id);
            if (Objects.isNull(promotionDomain)) {
                return null;
            }
            // 3. insert cache
            promotionCacheService.setPromotion(PromotionMapper.INSTANCE.toCacheDomain(promotionDomain));

        }
        // 4. get stock cache
        Long availableStock = stockService.getAvailableStock(id);
        if (Objects.isNull(availableStock)) {
            return null;
        }
        // 5. update stock onto available stock
        cacheDomain.setAvailableStock(availableStock);
        return PromotionMapper.INSTANCE.toDomain(cacheDomain);
    }
}
