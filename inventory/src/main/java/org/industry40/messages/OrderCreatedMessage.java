package org.industry40.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreatedMessage {
    private int orderId;
    private int productId;
    private int quantity;
}
