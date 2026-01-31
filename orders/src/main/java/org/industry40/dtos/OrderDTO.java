package org.industry40.dtos;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    private Integer id;
    private Integer userId;
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {
        private Integer productId;
        private Integer quantity;
    }
}
