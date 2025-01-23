package com.jptest.entity;

import com.jptest.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User class shows userID, name, email, status, createDate, createBy, updateDate, updateBy
 *
 */
@Entity
@Data
@Table(name = "MUser")
public class User {
    @Id
    private String userID;

    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /**
     * createDate will be fill-in when the first time creation
     */
    private LocalDateTime createDate;

    /**
     * createBy will be fill-in when the first time creation
     * the value of createBy is the userID
     */
    private String createBy;

    /**
     * updateDate will be fill-in or update when the user entity updated.
     */
    private LocalDateTime updateDate;

    /**
     * updateBy will be fill-in or update when the user entity updated.
     * the value of updateBy is the userID
     */
    private String updateBy;

}
