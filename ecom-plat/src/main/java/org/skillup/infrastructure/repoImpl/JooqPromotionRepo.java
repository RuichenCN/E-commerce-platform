package org.skillup.infrastructure.repoImpl;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.skillup.domain.promotion.PromotionDomain;
import org.skillup.domain.promotion.PromotionRepository;
import org.skillup.domain.promotion.stockStrategy.StockOperation;
import org.skillup.infrastructure.jooq.tables.Promotion;
import org.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository(value = "optimistic")
@Slf4j
public class JooqPromotionRepo implements PromotionRepository, StockOperation {
    @Autowired
    DSLContext dslContext;
    private static final Promotion P_T = new Promotion();
    @Override
    public void createPromotion(PromotionDomain domain) {
        dslContext.executeInsert(toRecord(domain));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PromotionDomain getPromotionById(String id) {
        return dslContext.selectFrom(P_T).where(P_T.PROMOTION_ID.eq(id)).fetchOptional(this::toDomain).orElse(null);
    }

    @Override
    public List<PromotionDomain> getByStatus(Integer status) {
        return dslContext.selectFrom(P_T).where(P_T.STATUS.eq(status)).fetch(this::toDomain);
    }

    @Override
    public void updatePromotion(PromotionDomain promotionDomain) {
        dslContext.executeUpdate(toRecord(promotionDomain));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id) {
//        update promotion
//        set available_stock = available_stock - 1, lock_stock = lock_stock + 1
//        where promotion_id = id and available_stock > 0;
        log.info("-----Optimistic-locking Strategy Lock Stock------");
        int isLocked = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.subtract(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.add(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isLocked == 1;
    }

    @Override
    public boolean revertStock(String id) {
        //        update promotion
//        set available_stock = available_stock + 1, lock_stock = lock_stock - 1
//        where promotion_id = id and lock_stock > 0;
        log.info("-----Optimistic-locking Strategy Revert Lock------");
        int isReverted = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.add(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return isReverted == 1;
    }

    @Override
    public boolean deductStock(String id) {
        //        update promotion
//        set lock_stock = lock_stock - 1
//        where promotion_id = id and lock_stock > 0;
        log.info("-----Optimistic-locking Strategy Deduct Stock------");
        int isDeducted = dslContext.update(P_T)
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return isDeducted == 1;
    }

    private PromotionRecord toRecord(PromotionDomain domain) {
        PromotionRecord promotionRecord = new PromotionRecord();
        promotionRecord.setPromotionId(domain.getPromotionId());
        promotionRecord.setPromotionName(domain.getPromotionName());
        promotionRecord.setCommodityId(domain.getCommodityId());
        promotionRecord.setOriginalPrice(domain.getOriginalPrice());
        promotionRecord.setPromotionPrice(domain.getPromotionalPrice());
        promotionRecord.setStartTime(domain.getStartTime());
        promotionRecord.setEndTime(domain.getEndTime());
        promotionRecord.setStatus(domain.getStatus());
        promotionRecord.setTotalStock(domain.getTotalStock());
        promotionRecord.setAvailableStock(domain.getAvailableStock());
        promotionRecord.setLockStock(domain.getLockStock());
        promotionRecord.setImageUrl(domain.getImageUrl());
        return promotionRecord;

    }
    private PromotionDomain toDomain(PromotionRecord record) {
        return PromotionDomain.builder()
                .promotionId(record.getPromotionId())
                .promotionName(record.getPromotionName())
                .commodityId(record.getCommodityId())
                .originalPrice(record.getOriginalPrice())
                .promotionalPrice(record.getPromotionPrice())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .status(record.getStatus())
                .totalStock(record.getTotalStock())
                .availableStock(record.getAvailableStock())
                .lockStock(record.getLockStock())
                .imageUrl(record.getImageUrl())
                .build();
    }
}
