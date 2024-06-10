package com.example.demo.service;

import com.example.demo.model.RewardHistory;
import com.example.demo.model.Tree;
import com.example.demo.model.dto.RewardDto;

import java.util.List;

public interface RewardsService {
    RewardDto getUserRewards(Long userId);

    void calculateTreePoints(Tree newTree);

    List<RewardHistory> getUserRewardsHistory(Long userId);
}
