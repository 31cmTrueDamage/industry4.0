package org.industry40.models;

import jakarta.persistence.*;
import lombok.Data;
import org.industry40.enums.JobStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "production_jobs")
public class ProductionJob {

    @Id
    private Integer planId;

    private Integer userId;

    private Integer orderId;
    private Integer productId;

    @ElementCollection
    private List<Integer> machineIds;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
}
