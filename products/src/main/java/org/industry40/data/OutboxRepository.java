package org.industry40.data;

import org.industry40.enums.ProductStatus;
import org.industry40.models.ProductOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<ProductOutbox, Integer> {

    List<ProductOutbox> findByStatus(ProductStatus status);
}
