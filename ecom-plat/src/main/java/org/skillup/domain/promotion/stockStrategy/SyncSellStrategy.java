package org.skillup.domain.promotion.stockStrategy;

import lombok.extern.slf4j.Slf4j;
import org.skillup.domain.promotion.PromotionDomain;
import org.skillup.domain.promotion.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "syncsell")
@Slf4j
public class SyncSellStrategy implements StockOperation{
    @Autowired
    PromotionRepository promotionRepository;
    @Override
    public boolean lockStock(String id) {
        synchronized (this) {
            log.info("-----Sync Sell Strategy------");
            // 1 get available stock, check > 0
            PromotionDomain currentPromotion = promotionRepository.getPromotionById(id);
            if (currentPromotion.getAvailableStock() <= 0) {
                return false;
            }
            log.info("-----Current Available Stock: {}------", currentPromotion.getAvailableStock());
            // 2 set available stock -1, set lock stock +1
            currentPromotion.setAvailableStock(currentPromotion.getAvailableStock() - 1);
            currentPromotion.setLockStock(currentPromotion.getLockStock() + 1);
            log.info("Update available stock to: {}, lock stock to: {}", currentPromotion.getAvailableStock(), currentPromotion.getLockStock());
            // 3 set domain
            promotionRepository.updatePromotion(currentPromotion);
            return true;

        }
    }

    @Override
    public boolean revertStock(String id) {
        return false;
    }

    @Override
    public boolean deductStock(String id) {
        return false;
    }
}
