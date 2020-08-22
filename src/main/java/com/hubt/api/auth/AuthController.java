package com.hubt.api.auth;

import com.hubt.api.auth.dto.JwtAuthenticationResponse;
import com.hubt.api.auth.dto.LoginRequest;
import com.hubt.api.auth.dto.LoginWithFaceBook;
import com.hubt.app.security.JwtTokenProvider;
import com.hubt.data.GbException;
import com.hubt.data.Response;
import com.hubt.data.model.Role;
import com.hubt.data.model.User;
import com.hubt.data.repository.RoleRepository;
import com.hubt.data.repository.UserRepository;
import com.hubt.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
@Slf4j
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = null;
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new GbException("username dose not exits"));
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return Response.error(HttpStatus.NOT_FOUND,"Tên đăng nhập hoặc mật khẩu không đúng");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken((User)authentication.getPrincipal());

        return Response.data(new JwtAuthenticationResponse(jwt,authentication.getPrincipal()));
    }

    @PostMapping("/login-facebook")
    public ResponseEntity loginWithFaceBook(@RequestBody LoginWithFaceBook loginWithFaceBook){
        try {
            authService.checkFbToken(loginWithFaceBook.getAccessToken());

            User user = null;
            Optional<User> optionalUser = userRepository.findByFacebookId(loginWithFaceBook.getUserID());
            if (optionalUser.isPresent()) user = optionalUser.get();
            else {
                user = new User();
                user.setUsername("fb"+loginWithFaceBook.getUserID());
                user.setPassword(passwordEncoder.encode(Long.toString(loginWithFaceBook.getUserID())));
                user.setFacebookId(loginWithFaceBook.getUserID());
                user.setAccountExpired(false);
                user.setAccountLocked(false);
                user.setEnable(true);

                Set<Role> roles = new HashSet<>();
                Role role = roleRepository.findByCode("ROLE_USER").orElseGet(Role::new);
                if (StringUtils.isEmpty(role.getCode())){
                    role.setCode("ROLE_USER");
                    roleRepository.save(role);
                }
                roles.add(role);
                user.setRoles(roles);

                userRepository.save(user);
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getFacebookId()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken((User) authentication.getPrincipal());

            return Response.data(new JwtAuthenticationResponse(jwt, authentication.getPrincipal()));

        }catch (Exception e){
            return Response.error(HttpStatus.BAD_REQUEST, "Login with facebook failed");
        }

    }

    @GetMapping("/random")
    public ResponseEntity randomStuff(){
        return Response.data("OK");
    }

    @GetMapping("/auth")
    public ResponseEntity confirmUser(Authentication authentication){
        return Response.data(authentication.getPrincipal());
    }
}
