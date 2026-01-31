package org.industry40.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.industry40.enums.PlanStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionPlan {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;

    @ElementCollection
    private List<Integer> assignedMachineIds;

    private LocalDateTime scheduledStartTime;
    private PlanStatus status;

}
