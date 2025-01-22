package com.jptest.service;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import com.jptest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void delete(User user){
        userRepository.delete(user);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public User findByUserIDAndStatusActive(String userID){
        return userRepository.findByUserIDAndStatus(userID, UserStatus.ACTIVE);
    }
}
