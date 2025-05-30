package com.e_learning.service;



import com.e_learning.model.User;
import com.e_learning.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username){
                return userRepository.findByUsername(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public List<User> getActiveUsers() {
        return userRepository.findByActive(1);
    }

}