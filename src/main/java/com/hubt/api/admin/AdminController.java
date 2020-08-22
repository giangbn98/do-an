package com.hubt.api.admin;

import com.hubt.api.admin.dto.UpdateUserRequest;
import com.hubt.data.GbException;
import com.hubt.data.Response;
import com.hubt.data.model.User;
import com.hubt.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/updateUser")
    public ResponseEntity updateUser(@RequestBody UpdateUserRequest request){
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new GbException("User does not exits"));
        if (!StringUtils.isEmpty(request.getEmail())){
            user.setEmail(request.getEmail());
        }
        if (!StringUtils.isEmpty(request.getPhoneNumber())){
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (!StringUtils.isEmpty(request.getNewPassword())){
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        user = userRepository.save(user);

        return Response.data(user);
    }
}
