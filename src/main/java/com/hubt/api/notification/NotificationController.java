package com.hubt.api.notification;

import com.hubt.data.Response;
import com.hubt.data.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity getNotification(@RequestParam long userId){
        return Response.data(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @PostMapping
    public ResponseEntity updateNotification(@RequestParam long notificationId){
        notificationRepository.setReadNotification(notificationId);
        return Response.data("ok");
    }
}
