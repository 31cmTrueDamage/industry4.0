package org.industry40.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.industry40.enums.MachineStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Table(name="machines")
@Entity
@Data
public class Machine {
    @Id
    private Integer id;
    private String type;
    private boolean isAvailable;

    @Enumerated(EnumType.STRING)
    private MachineStatus status;

    private LocalDateTime availableAt;
}
