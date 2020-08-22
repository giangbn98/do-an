package com.hubt.api.user;

import com.hubt.api.user.dto.CreateUserRequest;
import com.hubt.api.user.dto.UserRequest;
import com.hubt.data.GbException;
import com.hubt.data.Response;
import com.hubt.data.model.Role;
import com.hubt.data.model.User;
import com.hubt.data.repository.RoleRepository;
import com.hubt.data.repository.UserRepository;
import com.hubt.service.PushService;
import org.hibernate.annotations.CreationTimestamp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody CreateUserRequest userRequest){
        Optional<User> userOptional = userRepository.findByUsername(userRequest.getUsername());
        if (userOptional.isPresent()) throw new GbException("Tên đăng nhập đã tồn tại");
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setAccountLocked(false);
        user.setAccountExpired(false);
        user.setCredentialsExpired(false);
        user.setEnable(true);

        Set<Role> roles = new HashSet<>();
        Optional<Role> roleOptional = roleRepository.findByCode("ROLE_USER");
        if (roleOptional.isPresent()){
            roles.add(roleOptional.get());
        }else {
            Role role = new Role();
            role.setCode("ROLE_USER");
            role = roleRepository.save(role);
            roles.add(role);
        }

        user.setRoles(roles);
        user = userRepository.save(user);
        return Response.data(user);
    }

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody UserRequest userRequest){
        User user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new GbException("user dose not exits"));
        if (passwordEncoder.matches(userRequest.getOldPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(userRequest.getNewPassword()));
            userRepository.save(user);
            return Response.data(user);
        }else {
            return Response.error(HttpStatus.FOUND,"Mật khẩu cũ không đúng");
        }
    }

    @GetMapping
    public ResponseEntity findAll(@RequestParam int page,
                                  @RequestParam int pageSize,
                                  @RequestParam String searchKey){
        Page<User> userPage =
                userRepository.getUserByUsernameOrPhoneNumber(searchKey, PageRequest.of(page - 1, pageSize));
        return Response.data(userPage);
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody CreateUserRequest request){
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new GbException("User does not exits"));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user = userRepository.save(user);

        return Response.data(user);
    }
}
