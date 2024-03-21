package org.skillup.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.skillup.domain.promotion.PromotionDomain;
import org.skillup.domain.promotionCache.PromotionCacheDomain;

@Mapper
public interface PromotionMapper {
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);
    PromotionCacheDomain toCacheDomain(PromotionDomain promotionDomain);
}
