package org.industry40.dtos;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Integer userId;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private Integer productId;
        private Integer quantity;
    }
}