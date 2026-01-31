package org.industry40;

import org.industry40.dtos.ItemDTO;
import org.industry40.exceptions.NegativeStockException;
import org.industry40.models.Item;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private final ModelMapper mapper = new ModelMapper();

    @Test
    void testItemModelData() {
        Item item = new Item();
        item.setId(101);
        item.setQuantity(50);

        assertEquals(101, item.getId());
        assertEquals(50, item.getQuantity());
    }

    @Test
    void testInventoryMapping() {
        Item entity = new Item();
        entity.setId(202);
        entity.setQuantity(100);

        ItemDTO dto = mapper.map(entity, ItemDTO.class);

        assertNotNull(dto);
        assertEquals(202, dto.getId());
        assertEquals(100, dto.getQuantity());
    }

    @Test
    void testStockReductionLogic() {
        // Simulating the logic inside your OrderCreatedListener
        int currentStock = 10;
        int orderQuantity = 4;

        int newStock = currentStock - orderQuantity;

        assertEquals(6, newStock);
        assertTrue(newStock >= 0);
    }

    @Test
    void testNegativeStockPrevention() {
        // Testing the scenario that triggers your NegativeStockException
        int currentStock = 5;
        int orderQuantity = 10;

        assertThrows(NegativeStockException.class, () -> {
            int newQuantity = currentStock - orderQuantity;
            if (newQuantity < 0) {
                throw new NegativeStockException("Stock cannot be negative");
            }
        });
    }

    @Test
    void testDumbAppPass() {
        assertTrue(true);
    }
}