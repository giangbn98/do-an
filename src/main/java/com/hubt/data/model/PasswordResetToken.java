package com.hubt.data.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table
@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expiryDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(columnDefinition = "boolean default false")
    private boolean isUsed;

}
