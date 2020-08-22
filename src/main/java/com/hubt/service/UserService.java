package com.hubt.service;

import com.hubt.data.model.User;
import com.hubt.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) throw new UsernameNotFoundException(username);
        return userOptional.get();
    }

    public UserDetails loadUserById(long id){
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) throw new UsernameNotFoundException("Cannot find user from id "+ id);
        return userOptional.get();
    }


}
