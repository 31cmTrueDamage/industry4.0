package org.industry40.data;

import org.industry40.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByOrderId(Integer orderId);

    List<Notification> findByUserIdOrderByCreatedAtDesc(Integer userId);

    boolean existsByOrderIdAndType(Integer orderId, String type);
}
