package com.hubt.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(columnDefinition = "boolean default false")
    private boolean read;

    private String createdAt;

    private long postId;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
