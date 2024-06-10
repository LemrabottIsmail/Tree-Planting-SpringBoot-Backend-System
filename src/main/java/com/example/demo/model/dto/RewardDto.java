package com.example.demo.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RewardDto {

    private String ranking; // Silver (50) , Gold (150) , Diamond (400)
    private UserDto user;
    private String applicableRewards; // Silver: 100AED  Gold: 300AED, Diamond: 1000AED
    private int totalPoints;

}
