package com.example.demo.controller;

import com.example.demo.model.RewardHistory;
import com.example.demo.model.dto.RewardDto;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.demo.controller.AuthenticationController.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/get-ranking/{userId}")
    public RewardDto getUserRanking(@PathVariable Long userId, @RequestHeader(AUTHORIZATION_HEADER) Optional<String> bearerToken) {
        authenticationService.authenticateUser(bearerToken);
        return rewardsService.getUserRewards(userId);
    }

    @GetMapping("/reward-history/{userId}")
    public List<RewardHistory> getUserRewardsHistory(@PathVariable Long userId, @RequestHeader(AUTHORIZATION_HEADER) Optional<String> bearerToken) {
        authenticationService.authenticateUser(bearerToken);
        return rewardsService.getUserRewardsHistory(userId);
    }

}
