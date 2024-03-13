package org.skillup.application.order;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.skillup.domain.order.OrderDomain;
import org.skillup.domain.order.OrderService;
import org.skillup.domain.order.util.OrderStatus;
import org.skillup.domain.promotion.PromotionDomain;
import org.skillup.domain.promotion.PromotionService;
import org.skillup.infrastructure.jooq.tables.Promotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OrderApplication {
    @Autowired
    OrderService orderService;
    @Autowired
    PromotionService promotionService;
    @Transactional
    public OrderDomain createBuyNowOrder(OrderDomain orderDomain) {
        // 1. check promotion exist
        PromotionDomain promotionDomain = promotionService.getPromotionById(orderDomain.getPromotionId());
        if (Objects.isNull(promotionDomain)) {
            orderDomain.setOrderStatus(OrderStatus.ITEM_ERROR);
            return orderDomain;
        }
        // 2. lock promotion stock
        boolean isLocked = promotionService.lockStock(orderDomain.getPromotionId());
        if (!isLocked) {
            orderDomain.setOrderStatus(OrderStatus.OUT_OF_STOCK);
            return orderDomain;
        }
        // 3. fulfill order detail (create time...)
        orderDomain.setCreateTime(LocalDateTime.now());
        orderDomain.setOrderStatus(OrderStatus.CREATED);
        // 4. save into database
        orderService.createOrder(orderDomain);
        return orderDomain;
    }
}
