package org.industry40.services;

import org.industry40.data.NotificationRepository;
import org.industry40.exceptions.UnexistingNotificationException;
import org.industry40.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public Notification getById(Integer id) throws UnexistingNotificationException {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new UnexistingNotificationException("Notification not found with id: " + id));
    }

    public List<Notification> getByUserId(Integer userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(Integer id) {
        Notification n = getById(id);
        n.setRead(true);
        notificationRepository.save(n);
    }
}