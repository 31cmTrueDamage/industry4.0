package org.industry40.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.industry40.data.MachineRepository;
import org.industry40.enums.MachineStatus;
import org.industry40.exceptions.UnexistingMachineException;
import org.industry40.models.Machine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class MachinesService {

    @Autowired
    private MachineRepository machineRepository;

    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    @Transactional
    public void allocateMachine(Integer machineId, LocalDateTime until) {
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new UnexistingMachineException("Machine with ID " + machineId + " not found in the factory floor database."));

        machine.setStatus(MachineStatus.BUSY);
        machine.setAvailableAt(until);

        machineRepository.save(machine);

        log.info("Machine {} allocated until {}", machineId, until);
    }

    @Transactional
    public void releaseMachine(Integer machineId) {
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new UnexistingMachineException("Machine with ID " + machineId + " not found in the factory floor database."));

        machine.setStatus(MachineStatus.IDLE);
        machine.setAvailableAt(LocalDateTime.now());

        machineRepository.save(machine);
    }


}
