package com.jptest.repository;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUserIDAndStatus(String userID, UserStatus status);
}
