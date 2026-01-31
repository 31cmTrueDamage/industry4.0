package org.industry40.services;

import lombok.extern.slf4j.Slf4j;
import org.industry40.clients.MachineClient;
import org.industry40.dtos.MachineDTO;
import org.industry40.dtos.ScheduleResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlanningService {

    @Autowired
    private MachineClient machineClient;

    public ScheduleResultDTO calculateSchedule(int quantity) {
        List<MachineDTO> machines = machineClient.getAllMachines();

        int machinesNeeded = (int) Math.ceil(quantity / 50.0);

        if (machinesNeeded >= machines.size()) {
            log.warn("Order quantity {} exceeds total factory capacity. Capping to max machines.", quantity);
            machinesNeeded = machines.size();
        }

        List<MachineDTO> sortedMachines = machines.stream().
                sorted(Comparator.comparing(m -> m.isAvailable() ? LocalDateTime.now() : m.getAvailableAt()))
                .collect(Collectors.toList());

        List<MachineDTO> selectedMachines = sortedMachines.subList(0, machinesNeeded);
        List<Integer> selectedIds = selectedMachines.stream().map(MachineDTO::getId).collect(Collectors.toList());

        LocalDateTime startTime = selectedMachines.stream()
                .map(m -> m.isAvailable() ? LocalDateTime.now() : m.getAvailableAt())
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        log.info("Calculated Schedule: Need {} machines. Earliest start: {}", machinesNeeded, startTime);

        return new ScheduleResultDTO(startTime, selectedIds);
    }
}
