package org.industry40.services;

import org.industry40.data.ItemRepository;
import org.industry40.exceptions.UnexistingItemException;
import org.industry40.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private ItemRepository itemRepository;

    public Item getItem(Integer itemId) throws UnexistingItemException {
        return itemRepository.findById(itemId).orElseThrow(() ->  new UnexistingItemException("Item with id " + itemId + " does not exist"));
    }
}
