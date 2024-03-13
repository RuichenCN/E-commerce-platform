package org.skillup.apiPresentation.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInDto {

    private String promotionId;

    private String promotionName;

    private String userId;

    private Integer orderAmount;

}