package org.industry40.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductionPlanMessage {
    private Integer planId;
    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;

    private List<Integer> machineIds;

    private LocalDateTime scheduledStartTime;
    private Long durationInMinutes;
}
