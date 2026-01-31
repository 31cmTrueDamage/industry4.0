package org.industry40.config;

import lombok.extern.slf4j.Slf4j;
import org.industry40.data.MachineRepository;
import org.industry40.enums.MachineStatus;
import org.industry40.models.Machine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
public class DBSeeder {

    @Bean
    CommandLineRunner initDatabase(MachineRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                log.info("Seeding factory floor with 15 machines...");

                List<Machine> machines = List.of(
                        new Machine(1, "CNC", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(2, "CNC", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(3, "CNC", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(4, "CNC", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(5, "CNC", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(6, "DRILL", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(7, "DRILL", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(8, "ARM", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(9, "ARM", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(10, "ARM", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(11, "WELD", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(12, "WELD", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(13, "PRINT", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(14, "PRINT", true, MachineStatus.IDLE, LocalDateTime.now()),
                        new Machine(15, "SCAN", true, MachineStatus.IDLE, LocalDateTime.now())
                );

                repository.saveAll(machines);
                log.info("Factory floor ready with 15 fixed machines.");
            }
        };
    }
}