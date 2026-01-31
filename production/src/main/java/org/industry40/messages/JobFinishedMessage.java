package org.industry40.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFinishedMessage {
    private int orderId;
    private int userId;
    private Integer totalItemsInOrder;
}
