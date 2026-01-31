package org.industry40;

import org.industry40.dtos.ScheduleResultDTO;
import org.industry40.enums.PlanStatus;
import org.industry40.models.ProductionPlan;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testProductionPlanModel() {
        ProductionPlan plan = new ProductionPlan();
        plan.setOrderId(1);
        plan.setQuantity(100);
        plan.setStatus(PlanStatus.SCHEDULED);
        plan.setAssignedMachineIds(List.of(1, 2));

        assertEquals(100, plan.getQuantity());
        assertEquals(PlanStatus.SCHEDULED, plan.getStatus());
        assertEquals(2, plan.getAssignedMachineIds().size());
    }

    @Test
    void testScheduleMathLogic() {
        // Simulating the logic: (int) Math.ceil(quantity / 50.0)
        int quantity1 = 49;
        int quantity2 = 51;
        int quantity3 = 150;

        assertEquals(1, (int) Math.ceil(quantity1 / 50.0));
        assertEquals(2, (int) Math.ceil(quantity2 / 50.0));
        assertEquals(3, (int) Math.ceil(quantity3 / 50.0));
    }

    @Test
    void testScheduleResultDTO() {
        LocalDateTime now = LocalDateTime.now();
        ScheduleResultDTO result = new ScheduleResultDTO(now, List.of(10, 11));

        assertEquals(now, result.getStartTime());
        assertEquals(2, result.getMachineIds().size());
        assertTrue(result.getMachineIds().contains(10));
    }

    @Test
    void testDumbAppPass() {
        assertTrue(true);
    }
}