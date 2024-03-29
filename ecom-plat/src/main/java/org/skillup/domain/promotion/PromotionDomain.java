package org.skillup.domain.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDomain {
    private String promotionId;

    private String promotionName;

    private String commodityId;

    private Integer originalPrice;

    private Integer promotionalPrice;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /*
    0 not start, 1 active, 2 end
     */
    private Integer status;

    private Long totalStock;

    private Long availableStock;

    private Long lockStock;

    private String imageUrl;
}
