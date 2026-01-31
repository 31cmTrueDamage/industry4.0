package org.industry40;

import org.industry40.dtos.CustomerDTO;
import org.industry40.models.Customer;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private final ModelMapper mapper = new ModelMapper();

    @Test
    void testCustomerModelData() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Acme Corp");
        customer.setEmail("contact@acme.com");

        assertEquals(1, customer.getId());
        assertEquals("Acme Corp", customer.getName());
        assertEquals("contact@acme.com", customer.getEmail());
    }

    @Test
    void testCustomerDTOData() {
        CustomerDTO dto = new CustomerDTO();
        dto.setName("Cyberdyne Systems");
        dto.setIndustry("Robotics");

        assertEquals("Cyberdyne Systems", dto.getName());
        assertEquals("Robotics", dto.getIndustry());
    }

    @Test
    void testModelMapperMapping() {
        // Just checking if ModelMapper can handle your objects
        Customer entity = new Customer();
        entity.setId(50);
        entity.setName("Stark Industries");
        entity.setIndustry("Defense");
        entity.setCity("Malibu");

        CustomerDTO dto = mapper.map(entity, CustomerDTO.class);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getCity(), dto.getCity());
    }

    @Test
    void testDumbAppPass() {
        // The classic "it works" test
        assertTrue(true, "Logic should be sound");
    }
}