package org.skillup.apiPresentation;

import org.skillup.apiPresentation.dto.in.PromotionInDto;
import org.skillup.apiPresentation.dto.mapper.PromotionMapper;
import org.skillup.apiPresentation.dto.out.PromotionOutDto;
import org.skillup.apiPresentation.util.SkillUpCommon;
import org.skillup.application.promotion.PromotionApplication;
import org.skillup.domain.promotion.PromotionDomain;
import org.skillup.domain.promotion.PromotionService;
import org.skillup.domain.promotionCache.PromotionCacheDomain;
import org.skillup.domain.promotionCache.PromotionCacheService;
import org.skillup.domain.stock.StockDomain;
import org.skillup.domain.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    PromotionService promotionService;
    @Autowired
    PromotionApplication promotionApplication;
    @Autowired
    PromotionCacheService promotionCacheService;
    @Autowired
    StockService stockService;

    @PostMapping
    public PromotionOutDto createPromotion(@RequestBody PromotionInDto promotionInDto) {
        PromotionDomain promotionDomain = promotionService.createPromotion(PromotionMapper.INSTANCE.toDomain(promotionInDto));
        return PromotionMapper.INSTANCE.toOutDto(promotionDomain);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<PromotionOutDto> getPromotionById(@PathVariable("id") String id) {

        // cache aside read strategy
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status((SkillUpCommon.INTERNAL_ERROR)).body(null);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(PromotionMapper.INSTANCE.toOutDto(promotionDomain));
    }

    @GetMapping("/status/{status}")
    public List<PromotionOutDto> getByStatus(@PathVariable("status") Integer status) {
        List<PromotionDomain> promotionDomainList = promotionService.getByStatus(status);
        return promotionDomainList.stream().map(PromotionMapper.INSTANCE::toOutDto).toList();
    }

    @PostMapping("/lock/id/{id}")
    public ResponseEntity<Boolean> lockStock(@PathVariable("id") String id) {
        // 1 check promotion exist in cache
        PromotionCacheDomain promotionCacheDomain = promotionCacheService.getPromotionById(id);
        if (Objects.isNull(promotionCacheDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(false);
        }
        // 2 try to lock stock in cache
        StockDomain stockDomain = StockDomain.builder().promotionId(promotionCacheDomain.getPromotionId()).build();
        boolean isLocked = stockService.lockStock(stockDomain);
        if (isLocked) {
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(true);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(false);
    }

    @PostMapping("/revert/id/{id}")
    public ResponseEntity<Boolean> revertStock(@PathVariable("id") String id) {
        // 1 check promotion exist in cache
        PromotionCacheDomain promotionCacheDomain = promotionCacheService.getPromotionById(id);
        if (Objects.isNull(promotionCacheDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(false);
        }
        // 2 try to lock stock in cache
        StockDomain stockDomain = StockDomain.builder().promotionId(promotionCacheDomain.getPromotionId()).build();
        // 2 try to revert stock
        boolean isReverted = stockService.revertStock(stockDomain);
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
}
