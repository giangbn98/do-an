package com.hubt.data.repository;

import com.hubt.data.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(long userId);

    @Modifying
    @Transactional
    @Query("update Notification set read = true where id = :id")
    void setReadNotification(@Param("id") long id);
}
