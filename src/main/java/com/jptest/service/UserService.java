package com.jptest.service;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import com.jptest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling operations related to {@link User} entity.
 * This class provides methods to perform CRUD operations on users, such as saving,
 * deleting, retrieving all users, and finding a user by userID and status.
 */
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository  userRepository;

    /**
     * Deletes the specified user from the repository.
     *
     * @param user the user to be deleted
     */
    public void delete(User user){
        userRepository.delete(user);
    }

    /**
     * Saves the specified user to the repository.
     * If the user is new, it will be added; otherwise, it will be updated.
     *
     * @param user the user to be saved
     */
    public void save(User user){
        userRepository.save(user);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users
     */
    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    /**
     * Finds a user by their userID and filters by active status.
     *
     * @param userID the userID of the user to search for
     * @return the user matching the userID and active status, or null if not found
     */
    public Optional<User> findByUserIDAndStatusActive(String userID){
        return userRepository.findByUserIDAndStatus(userID, UserStatus.ACTIVE);
    }
}
