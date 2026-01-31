package org.industry40.data;

import org.industry40.models.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<ProductionPlan, Integer> {
}
