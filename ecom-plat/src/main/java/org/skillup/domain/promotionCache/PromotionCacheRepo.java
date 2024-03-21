package org.skillup.domain.promotionCache;

public interface PromotionCacheRepo {
    public PromotionCacheDomain getPromotionById(String id);
    public void setPromotion(PromotionCacheDomain cacheDomain);
}
