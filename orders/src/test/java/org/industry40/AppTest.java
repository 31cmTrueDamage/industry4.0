package org.industry40;

import org.industry40.enums.OrderStatus;
import org.industry40.messages.OrderCreatedMessage;
import org.industry40.models.Order;
import org.industry40.models.OrderItem;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testOrderInitializationAndStatus() {
        Order order = new Order();
        order.setId(1);
        order.setUserId(99);
        order.setStatus(OrderStatus.CREATED);

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(99, order.getUserId());
    }

    @Test
    void testOrderItemRelationship() {
        Order order = new Order();
        order.setItems(new ArrayList<>());

        OrderItem item = new OrderItem();
        item.setProductId(500);
        item.setQuantity(2);
        item.setOrder(order);

        order.getItems().add(item);

        assertEquals(1, order.getItems().size());
        assertEquals(500, order.getItems().get(0).getProductId());
        assertEquals(order, order.getItems().get(0).getOrder());
    }

    @Test
    void testOrderCreatedMessageStructure() {
        // Ensuring the MQ message matches the expected schema
        OrderCreatedMessage msg = new OrderCreatedMessage();
        msg.setOrderId(10);
        msg.setUserId(99);
        msg.setProductId(500);
        msg.setQuantity(5);

        assertEquals(10, msg.getOrderId());
        assertEquals(99, msg.getUserId());
        assertEquals(500, msg.getProductId());
    }

    @Test
    void testDumbAppPass() {
        assertTrue(true, "Base test logic should hold");
    }
}