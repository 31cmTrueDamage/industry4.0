package org.industry40;

import org.industry40.enums.ProductStatus;
import org.industry40.models.Product;
import org.industry40.models.ProductOutbox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testProductModelData() {
        Product product = new Product();
        product.setId(10);
        product.setName("Industrial Drill");
        product.setStock(50);
        product.setPrice(1200);

        assertEquals("Industrial Drill", product.getName());
        assertEquals(50, product.getStock());
    }

    @Test
    void testOutboxEntityMapping() {
        ProductOutbox outbox = new ProductOutbox();
        outbox.setProductId(10);
        outbox.setStatus(ProductStatus.PENDING);

        assertNotNull(outbox);
        assertEquals(ProductStatus.PENDING, outbox.getStatus());
        assertEquals(10, outbox.getProductId());
    }

    @Test
    void testDumbAppPass() {
        assertTrue(true);
    }
}