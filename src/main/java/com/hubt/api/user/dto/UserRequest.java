package com.hubt.api.user.dto;

import lombok.Data;

@Data
public class UserRequest {

    private String username;

    private String oldPassword;

    private String newPassword;
}
