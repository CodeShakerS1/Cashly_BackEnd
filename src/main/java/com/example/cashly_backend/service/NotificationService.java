package com.example.cashly_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Notification;
import com.example.cashly_backend.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public List<Notification> findAll() {
        return repository.findAll();
    }

    public Optional<Notification> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Notification> findByUser(Integer userId) {
        return repository.findByUserId(userId);
    }

    public List<Notification> findUnreadNotificationsByUser(Integer userId) {
        return repository.findByUserIdAndIsRead(userId, false);
    }

    public Notification registerNotification(Notification notification) {
        return repository.save(notification);
    }

    public Notification createNotification(String title, String message, String type, Integer userId) {
        Notification notif = new Notification();
        notif.setTitle(title);
        notif.setMessage(message);
        notif.setType(type);
        notif.setUserId(userId);
        notif.setIsRead(false);
        notif.setCreatedAt(LocalDateTime.now());
        
        return this.registerNotification(notif);
    }

    public Notification markAsReadNotification(Integer id) {
    Notification notif = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
    notif.setIsRead(true);
    return repository.save(notif); 
}

    public void deleteNotification(Integer id) {
        repository.deleteById(id);
    }
}
