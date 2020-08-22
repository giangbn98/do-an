package com.hubt.data;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GbException extends RuntimeException {

    private HttpStatus httpStatus;
    private String userMessage;
    private String code;

    public GbException(String userMessage) {
        super(userMessage);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.userMessage = userMessage;
    }

    public GbException(HttpStatus httpStatus, String userMessage) {
        super(userMessage);
        this.httpStatus = httpStatus;
        this.userMessage = userMessage;
    }
}
