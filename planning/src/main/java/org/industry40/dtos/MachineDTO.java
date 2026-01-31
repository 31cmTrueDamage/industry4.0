package org.industry40.dtos;

import lombok.Getter;
import lombok.Setter;
import org.industry40.enums.MachineStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class MachineDTO {
    private Integer id;
    private String type;
    private boolean isAvailable;
    private MachineStatus status;
    private LocalDateTime availableAt;
}
