package org.skillup.apiPresentation;

import org.skillup.apiPresentation.dto.in.PromotionInDto;
import org.skillup.apiPresentation.dto.out.PromotionOutDto;
import org.skillup.apiPresentation.util.SkillUpCommon;
import org.skillup.domain.promotion.PromotionDomain;
import org.skillup.domain.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    PromotionService promotionService;

    @PostMapping
    public PromotionOutDto createPromotion(@RequestBody PromotionInDto promotionInDto) {
        PromotionDomain promotionDomain = promotionService.createPromotion(toDomain(promotionInDto));
        return toOutDto(promotionDomain);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<PromotionOutDto> getPromotionById(@PathVariable("id") String id) {
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status((SkillUpCommon.INTERNAL_ERROR)).body(null);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(toOutDto(promotionDomain));
    }

    @GetMapping("/status/{status}")
    public List<PromotionOutDto> getByStatus(@PathVariable("status") Integer status) {
        List<PromotionDomain> promotionDomainList = promotionService.getByStatus(status);
        return promotionDomainList.stream().map(this::toOutDto).toList();
    }

    @PostMapping("/lock/id/{id}")
    public ResponseEntity<Boolean> lockStock(@PathVariable("id") String id) {
        // 1 check promotion exist
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(false);
        }
        // 2 try to lock stock
        boolean isLocked = promotionService.lockStock(id);
        if (isLocked) {
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(true);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(false);
    }

    @PostMapping("/revert/id/{id}")
    public ResponseEntity<Boolean> revertStock(@PathVariable("id") String id) {
        // 1 check promotion exist
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(false);
        }
        // 2 try to revert stock
        boolean isReverted = promotionService.revertStock(id);
        if (isReverted) {
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(true);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(false);
    }

    @PostMapping("/deduct/id/{id}")
    public ResponseEntity<Boolean> deductStock(@PathVariable("id") String id) {
        // 1 check promotion exist
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(false);
        }
        // 2 try to deduct stock
        boolean isDeducted = promotionService.deductStock(id);
        if (isDeducted) {
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(true);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(false);
    }

    private PromotionDomain toDomain(PromotionInDto promotionInDto) {
        return PromotionDomain.builder()
                .promotionId(UUID.randomUUID().toString())
                .promotionName(promotionInDto.getPromotionName())
                .commodityId(promotionInDto.getCommodityId())
                .startTime(promotionInDto.getStartTime())
                .endTime(promotionInDto.getEndTime())
                .originalPrice(promotionInDto.getOriginalPrice())
                .promotionalPrice(promotionInDto.getPromotionalPrice())
                .totalStock(promotionInDto.getTotalStock())
                .availableStock(promotionInDto.getAvailableStock())
                .lockStock(promotionInDto.getLockStock())
                .imageUrl(promotionInDto.getImageUrl())
                .status(promotionInDto.getStatus())
                .build();
    }
    private PromotionOutDto toOutDto(PromotionDomain promotionDomain) {
        return PromotionOutDto.builder()
                .promotionId(promotionDomain.getPromotionId())
                .promotionName(promotionDomain.getPromotionName())
                .commodityId(promotionDomain.getCommodityId())
                .startTime(promotionDomain.getStartTime())
                .endTime(promotionDomain.getEndTime())
                .totalStock(promotionDomain.getTotalStock())
                .availableStock(promotionDomain.getAvailableStock())
                .originalPrice(promotionDomain.getOriginalPrice())
                .promotionalPrice(promotionDomain.getPromotionalPrice())
                .lockStock(promotionDomain.getLockStock())
                .imageUrl(promotionDomain.getImageUrl())
                .status(promotionDomain.getStatus())
                .build();
    }
}
