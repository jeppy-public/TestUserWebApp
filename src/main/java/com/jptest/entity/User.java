package com.jptest.entity;

import com.jptest.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "MUser")
public class User {
    @Id
    private String userID;

    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime createDate;
    private String createBy;

    private LocalDateTime updateDate;
    private String updateBy;

}
