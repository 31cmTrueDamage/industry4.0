package org.industry40.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ScheduleResultDTO {
    private LocalDateTime startTime;
    private List<Integer> machineIds;
}
