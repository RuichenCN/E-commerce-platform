package org.skillup.domain.promotion;

import java.util.List;

public interface PromotionRepository {
    void createPromotion(PromotionDomain domain);

    PromotionDomain getPromotionById(String id);

    List<PromotionDomain> getByStatus(Integer status);
    void updatePromotion(PromotionDomain promotionDomain);
}
