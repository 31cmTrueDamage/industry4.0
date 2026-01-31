package org.industry40.controllers;

import org.industry40.dtos.ItemDTO;
import org.industry40.models.Item;
import org.industry40.services.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    private ModelMapper modelMapper;

    public InventoryController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{itemId}")
    ResponseEntity<ItemDTO> getItem(@PathVariable Integer itemId) {
        Item item = inventoryService.getItem(itemId);
        return  ResponseEntity.ok(this.modelMapper.map(item, ItemDTO.class));
    }
}
