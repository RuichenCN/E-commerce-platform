package org.skillup.infrastructure.repoImpl;

import org.jooq.DSLContext;
import org.skillup.domain.order.OrderDomain;
import org.skillup.domain.order.OrderRepository;
import org.skillup.domain.order.util.OrderStatus;
import org.skillup.infrastructure.jooq.tables.Order;
import org.skillup.infrastructure.jooq.tables.records.OrderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JooqOrderRepo implements OrderRepository {
    @Autowired
    DSLContext dslContext;
    private static final Order OT = new Order();
    @Override
    public void createOrder(OrderDomain orderDomain) {
        dslContext.executeInsert(toRecord(orderDomain));
    }

    @Override
    public OrderDomain getOrderById(Long id) {
        return dslContext.selectFrom(OT).where(OT.ORDER_NUMBER.eq(id)).fetchOptional(this::toDomain).orElse(null);
    }

    @Override
    public void updateOrder(OrderDomain orderDomain) {
        // TODO Auto-generated method stub
    }
    private OrderDomain toDomain(OrderRecord orderRecord) {
        return OrderDomain.builder()
                .orderNumber(orderRecord.getOrderNumber())
                .orderStatus(OrderStatus.CACHE.get(orderRecord.getOrderStatus()))
                .promotionId(orderRecord.getPromotionId())
                .promotionName(orderRecord.getPromotionName())
                .userId(orderRecord.getUserId())
                .orderAmount(orderRecord.getOrderAmount())
                .createTime(orderRecord.getCreateTime())
                .payTime(orderRecord.getPayTime())
                .build();
    }
    private OrderRecord toRecord(OrderDomain orderDomain) {
        return new OrderRecord(
                orderDomain.getOrderNumber(),
                orderDomain.getOrderStatus().code,
                orderDomain.getPromotionId(),
                orderDomain.getPromotionName(),
                orderDomain.getUserId(),
                orderDomain.getOrderAmount(),
                orderDomain.getCreateTime(),
                orderDomain.getPayTime()
        );
    }
}
