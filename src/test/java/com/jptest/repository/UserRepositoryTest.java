package com.jptest.repository;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@Tag("repository")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setUserID("U001");
        user1.setName("Amir Sudiman");
        user1.setEmail("amir.sudiman@google.com");
        user1.setStatus(UserStatus.ACTIVE);
        user1.setCreateDate(LocalDateTime.now());
        user1.setCreateBy("admin");

        user2 = new User();
        user2.setUserID("U002");
        user2.setName("George Ransom");
        user2.setEmail("george.ransom@microsoft.com");
        user2.setStatus(UserStatus.ACTIVE);
        user2.setCreateDate(LocalDateTime.now());
        user2.setCreateBy("admin");
    }

    @Test
    void userRepository_FindByUserIDAndStatus() {
        userRepository.save(user1);
        User foundUser = userRepository.findByUserIDAndStatus(user1.getUserID(), user1.getStatus())
                .orElseThrow(() -> new RuntimeException("User not exist"));

        assertNotNull(foundUser);
        assertEquals(user1.getUserID(), foundUser.getUserID());
    }
}