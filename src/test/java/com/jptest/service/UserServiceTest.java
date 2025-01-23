package com.jptest.service;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import com.jptest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Tag("service")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(this.userRepository);

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
    void userService_Delete() {
        doNothing().when(userRepository).delete(user1);

        userService.delete(user1);

        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    void userService_Save() {
        when(userRepository.save(user1)).thenReturn(user1);

        userService.save(user1);

        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void userService_FindAllUser() {
        when(this.userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = this.userService.findAllUser();
        assertNotNull(users);
        assertEquals(2, users.size());

        verify(this.userRepository, times(1)).findAll();

    }

    @Test
    void userService_FindByUserIDAndStatusActive() {
        when(userRepository.findByUserIDAndStatus(user1.getUserID(), UserStatus.ACTIVE)).thenReturn(Optional.ofNullable(user1));

        Optional<User> foundUser = userService.findByUserIDAndStatusActive(user1.getUserID());

        assertNotNull(foundUser);
        assertTrue(foundUser.isPresent(), "User should be present");
        assertEquals(user1.getUserID(), foundUser.get().getUserID());

        verify(userRepository, times(1)).findByUserIDAndStatus(user1.getUserID(), UserStatus.ACTIVE);
    }
}