package org.skillup.apiPresentation.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOutDto {
    private String orderNumber;

    //订单状态: -2: promotion invalid, -1:库存不足订单, 0:预订单，1:已创建等待付款, 2表示已付款, 3订单过期或者无效
    private Integer orderStatus;

    private String promotionId;

    private String promotionName;

    private String userId;

    private Integer orderAmount;

    private LocalDateTime createTime;

    private LocalDateTime payTime;
}