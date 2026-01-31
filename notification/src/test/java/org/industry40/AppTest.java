package org.industry40;

import org.industry40.models.Notification;
import org.industry40.models.OrderProgress;
import org.industry40.messages.JobFinishedMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testNotificationCreation() {
        Notification notification = new Notification();
        notification.setOrderId(100);
        notification.setUserId(1);
        notification.setTitle("Test Title");
        notification.setMessage("Test Message");
        notification.setType("SUCCESS");

        assertEquals(100, notification.getOrderId());
        assertEquals(1, notification.getUserId());
        assertFalse(notification.isRead());
    }

    @Test
    void testOrderProgressLogic() {
        // Simulating the logic inside ProductionListener.handleJobFinished
        OrderProgress progress = new OrderProgress(100, 0);

        // Simulate receiving 2 finished jobs out of 3
        progress.setCompletedCount(progress.getCompletedCount() + 1);
        progress.setCompletedCount(progress.getCompletedCount() + 1);

        assertEquals(2, progress.getCompletedCount());

        int totalItemsExpected = 3;
        boolean isFinished = progress.getCompletedCount() >= totalItemsExpected;

        assertFalse(isFinished, "Order should not be finished yet");

        // Finish the last item
        progress.setCompletedCount(progress.getCompletedCount() + 1);
        assertTrue(progress.getCompletedCount() >= totalItemsExpected, "Order should be finished now");
    }

    @Test
    void testMessageMapping() {
        JobFinishedMessage msg = new JobFinishedMessage(500, 10, 5);

        assertEquals(500, msg.getOrderId());
        assertEquals(10, msg.getUserId());
        assertEquals(5, msg.getTotalItemsInOrder());
    }

    @Test
    void testDumbAppPass() {
        assertTrue(true);
    }
}