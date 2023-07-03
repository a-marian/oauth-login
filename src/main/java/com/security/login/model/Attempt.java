package com.security.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="LOGIN_ATTEMPTS")
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private int count;
    @OneToOne(mappedBy = "failedAttempts")
    private User user;

}
