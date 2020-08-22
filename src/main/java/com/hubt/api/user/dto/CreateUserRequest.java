package com.hubt.api.user.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

}
