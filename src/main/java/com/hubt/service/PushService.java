package com.hubt.service;

import com.hubt.data.model.Notification;
import com.hubt.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PushService {
    private final SimpMessagingTemplate messagingTemplate;


    public PushService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void pushNotification(Long userId, Notification notification){
        messagingTemplate.convertAndSend("/queue/notification/" + userId , notification);
    }
}
