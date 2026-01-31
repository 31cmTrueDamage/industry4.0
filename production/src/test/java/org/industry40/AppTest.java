package org.industry40;

import org.industry40.enums.JobStatus;
import org.industry40.messages.JobFinishedMessage;
import org.industry40.models.ProductionJob;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testProductionJobState() {
        ProductionJob job = new ProductionJob();
        job.setPlanId(1001);
        job.setStatus(JobStatus.PENDING);
        job.setMachineIds(List.of(1, 2, 3));

        assertEquals(JobStatus.PENDING, job.getStatus());
        assertEquals(3, job.getMachineIds().size());

        // Test state transition
        job.setStatus(JobStatus.IN_PROGRESS);
        assertEquals(JobStatus.IN_PROGRESS, job.getStatus());
    }

    @Test
    void testJobTimingCalculation() {
        ProductionJob job = new ProductionJob();
        LocalDateTime now = LocalDateTime.now();
        job.setStartTime(now);
        job.setEndTime(now.plusMinutes(30));

        assertTrue(job.getEndTime().isAfter(job.getStartTime()));
        assertEquals(now.plusMinutes(30), job.getEndTime());
    }

    @Test
    void testFinishedMessagePayload() {
        JobFinishedMessage msg = new JobFinishedMessage(50, 10, 5);

        assertEquals(50, msg.getOrderId());
        assertEquals(10, msg.getUserId());
        assertEquals(5, msg.getTotalItemsInOrder());
    }

    @Test
    void testDumbAppPass() {
        assertTrue(true);
    }
}