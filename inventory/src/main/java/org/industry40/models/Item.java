package org.industry40.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name="inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name="last_modified")
    private LocalDateTime lastModified;
}
