package com.jptest.repository;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserIDAndStatus(String userID, UserStatus status);
}
