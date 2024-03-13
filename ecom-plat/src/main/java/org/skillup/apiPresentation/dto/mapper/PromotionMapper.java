package org.skillup.apiPresentation.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.skillup.apiPresentation.dto.in.PromotionInDto;
import org.skillup.apiPresentation.dto.out.PromotionOutDto;
import org.skillup.domain.promotion.PromotionDomain;

@Mapper
public interface PromotionMapper {
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);
    @Mapping(source = "promotionName", target = "promotionName")
    PromotionDomain toDomain(PromotionInDto promotionInDto);
    PromotionOutDto toOutDto(PromotionDomain promotionDomain);
}
