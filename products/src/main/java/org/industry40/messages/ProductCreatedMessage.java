package org.industry40.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductCreatedMessage {

    private Integer messageId;

    private Integer productId;

    private Integer stock;
}
