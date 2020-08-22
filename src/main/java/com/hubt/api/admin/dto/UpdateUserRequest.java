package com.hubt.api.admin.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private long id;

    private String newPassword;

    private String phoneNumber;

    private String email;
}
