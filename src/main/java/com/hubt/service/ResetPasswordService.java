package com.hubt.service;

import com.hubt.data.GbException;
import com.hubt.data.model.PasswordResetToken;
import com.hubt.data.repository.PasswordResetTokenRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetPasswordService {

    @Autowired
    private PasswordResetTokenRepostitory passwordResetTokenRepostitory;

    public String generateToken(){
        String token = UUID.randomUUID().toString();
        while (true){
            Optional<PasswordResetToken> passwordResetTokenOptional
                    = passwordResetTokenRepostitory.findByToken(token);
            if (!passwordResetTokenOptional.isPresent()){
                break;
            }
        }
        return token;
    }

    public boolean validTokenResetPassword(String code){
        PasswordResetToken passwordReset
                = passwordResetTokenRepostitory.findByToken(code)
                .orElseThrow(() -> new GbException("code is valid"));
        return passwordReset.getExpiryDate().after(new Date());
    }
}
