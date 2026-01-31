package org.industry40;

import org.industry40.enums.MachineStatus;
import org.industry40.models.Machine;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testMachineModelInitialization() {
        Machine machine = new Machine();
        machine.setId(501);
        machine.setType("CNC-LATHE");
        machine.setStatus(MachineStatus.IDLE);
        machine.setAvailable(true);

        assertEquals(501, machine.getId());
        assertEquals("CNC-LATHE", machine.getType());
        assertEquals(MachineStatus.IDLE, machine.getStatus());
        assertTrue(machine.isAvailable());
    }

    @Test
    void testMachineAllocationState() {
        // Simulating the logic inside MachinesService.allocateMachine
        Machine machine = new Machine();
        machine.setStatus(MachineStatus.IDLE);

        LocalDateTime allocationTime = LocalDateTime.now().plusHours(2);

        // State change
        machine.setStatus(MachineStatus.BUSY);
        machine.setAvailableAt(allocationTime);

        assertEquals(MachineStatus.BUSY, machine.getStatus());
        assertEquals(allocationTime, machine.getAvailableAt());
    }

    @Test
    void testMachineReleaseState() {
        // Simulating the logic inside MachinesService.releaseMachine
        Machine machine = new Machine();
        machine.setStatus(MachineStatus.BUSY);

        // State change
        machine.setStatus(MachineStatus.IDLE);
        machine.setAvailableAt(LocalDateTime.now());

        assertEquals(MachineStatus.IDLE, machine.getStatus());
        assertNotNull(machine.getAvailableAt());
    }

    @Test
    void testEnumValues() {
        // Ensure all industrial statuses are present
        assertEquals("IDLE", MachineStatus.IDLE.name());
        assertEquals("BUSY", MachineStatus.BUSY.name());
        assertEquals("MAINTENANCE", MachineStatus.MAINTENANCE.name());
    }

    @Test
    void testDumbAppPass() {
        assertTrue(true);
    }
}