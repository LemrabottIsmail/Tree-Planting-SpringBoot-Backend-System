package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "usersessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class UserSession {
    @Id
    private Long userId;
    private String token;
    private LocalDateTime createdAt;
}
