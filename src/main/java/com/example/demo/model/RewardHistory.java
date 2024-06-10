package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "rewardshistory")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class RewardHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardId;
    private Long userId;
    private Long treeId;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
