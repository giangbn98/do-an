package com.hubt.service;

import com.hubt.data.GbException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${facebook.appId}")
    private String appId;

    @Value("${facebook.appSecret}")
    private String appSecret;

    public void checkFbToken(String token){
        final String URL_CHECK_TOKEN = "https://graph.facebook.com/debug_token?input_token=" + token
                + "&access_token=" + appId + "|" + appSecret;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange(URL_CHECK_TOKEN, HttpMethod.GET, httpEntity, Object.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GbException("Login facebook failed");
        }
    }
}
