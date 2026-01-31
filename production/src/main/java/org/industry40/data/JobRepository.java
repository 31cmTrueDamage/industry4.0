package org.industry40.data;

import org.industry40.models.ProductionJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<ProductionJob, Integer> {

    List<ProductionJob> findByOrderId(Integer orderId);

    long countByOrderId(Integer orderId);
}
