package com.example.demo.model.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private Long userId;
    private String firstName;
    private String lastName; // last_name
    private String username; // user_name
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int points;

}
