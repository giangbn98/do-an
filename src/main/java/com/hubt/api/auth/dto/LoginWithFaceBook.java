package com.hubt.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"signedRequest", "graphDomain"})
public class LoginWithFaceBook {

    private String accessToken;

    private long data_access_expiration_time;

    private long expiresIn;

    private long userID;
}
