package org.industry40.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @Column(nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private Integer quantity;
}
