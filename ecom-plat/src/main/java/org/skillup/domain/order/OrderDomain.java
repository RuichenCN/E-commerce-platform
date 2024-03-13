package org.skillup.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.skillup.domain.order.util.OrderStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDomain {
    private Long orderNumber;

    //订单状态: -2: promotion invalid, -1:库存不足订单, 0:预订单，1:已创建等待付款, 2表示已付款, 3订单过期或者无效
    private OrderStatus orderStatus;

    private String promotionId;

    private String promotionName;

    private String userId;

    private Integer orderAmount;

    private LocalDateTime createTime;

    private LocalDateTime payTime;
}
