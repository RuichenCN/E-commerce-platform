package org.skillup.application.promotion;

import lombok.extern.slf4j.Slf4j;
import org.skillup.application.mapper.PromotionMapper;
import org.skillup.domain.promotion.PromotionDomain;
import org.skillup.domain.promotion.PromotionService;
import org.skillup.domain.promotionCache.PromotionCacheService;
import org.skillup.domain.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PromotionPreHeatApplication implements ApplicationRunner {
    @Autowired
    PromotionService promotionService;
    @Autowired
    StockService stockService;
    @Autowired
    PromotionCacheService promotionCacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("---- Cache preheat init active promotion stock into cache ----");
        // 1. Search active promotions
        List<PromotionDomain> activePromotions = promotionService.getByStatus(1);
        activePromotions.forEach(promotionDomain -> {
            // 2. Set available stock to cache
            stockService.setAvailableStock(promotionDomain.getPromotionId(), promotionDomain.getAvailableStock());
            // 3. Set promotion cache domain to cache
            promotionCacheService.setPromotion(PromotionMapper.INSTANCE.toCacheDomain(promotionDomain));

        });
    }
}
