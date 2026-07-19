package com.smartpos.backend.service;

import com.smartpos.backend.entity.User;
import com.smartpos.backend.exceptions.ResourceNotFoundException;
import com.smartpos.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers(){
        List<User> users=userRepository.findAll();
        return users;
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
    }

    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id,User user){
        User userData=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
        userData.setUsername(user.getUsername());
        userData.setPassword(user.getPassword());
//        userData.setRole(user.getRole());
        return userRepository.save(userData);
    }

    public void deleteUserById(Long id){
        User userData=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
        userRepository.delete(userData) ;
    }
}
