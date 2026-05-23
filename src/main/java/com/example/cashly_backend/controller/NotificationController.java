package com.example.cashly_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cashly_backend.entity.Notification;
import com.example.cashly_backend.service.NotificationService;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notification")
    public ResponseEntity<String> createNotification(@RequestBody Notification notification) {
        notificationService.createNotification(
            notification.getTitle(),
            notification.getMessage(),
            notification.getType(),
            notification.getUserId()
        );
        return ResponseEntity.status(201).body("Notification created successfully!");
    }

    @GetMapping("/notification")
    public ResponseEntity<List<Notification>> getNotifications() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    @GetMapping("/notification/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Integer id) {
        return notificationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/notification/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.findByUser(userId));
    }

    @GetMapping("/notification/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.findUnreadNotificationsByUser(userId));
    }

    @PatchMapping("/notification/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Integer id) {
        if (notificationService.findById(id).isPresent()) {
            notificationService.markAsReadNotification(id);
            return ResponseEntity.ok("Notification marked as read!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/notification/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id) {
        if (notificationService.findById(id).isPresent()) {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}