package com.example.demo.service.impl;

import com.example.demo.model.RewardHistory;
import com.example.demo.model.Tree;
import com.example.demo.model.User;
import com.example.demo.model.dto.RewardDto;
import com.example.demo.model.dto.TreeDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.repository.RewardHistoryRepository;
import com.example.demo.service.RewardsService;
import com.example.demo.service.UserService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RewardsServiceImpl implements RewardsService {

    private Map<Long, List<RewardHistory>> userRewardHistory;

    @PostConstruct
    public void init(){
        long timeToLiveMinutes = 5;
        userRewardHistory = new PassiveExpiringMap<>(timeToLiveMinutes * 60 * 1000);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private RewardHistoryRepository rewardHistoryRepository;


    @Override
    public RewardDto getUserRewards(Long userId) {

        UserDto user = userService.getUserById(userId);

        String userRanking = "No Ranking";
        String applicableRewards = "No Rewards";

        int userPoints = user.getPoints();

        if (userPoints >= 50) {
            userRanking = "Silver";
            applicableRewards = "100AED";
        }
        if (userPoints >= 150) {
            userRanking = "Gold";
            applicableRewards = "300AED";
        }
        if (userPoints >= 400) {
            userRanking = "Diamond";
            applicableRewards = "1000AED";
        }

        RewardDto userReward = new RewardDto();
        userReward.setRanking(userRanking);
        userReward.setUser(user);
        userReward.setApplicableRewards(applicableRewards);
        userReward.setTotalPoints(userPoints);

        return userReward;

    }

    @Override
    public void calculateTreePoints(Tree newTree) {

        User user = userService.getSavedUserById(newTree.getUserId());

        int userPreviousPoints = user.getPoints();

        int addedPoints = 0;

        String treeSpecies = newTree.getSpecies();

        if (treeSpecies.equals("Date Palm")) {
            addedPoints += 5;
        } else if (treeSpecies.equals("Ghaff")) {
            addedPoints += 4;
        } else {
            addedPoints += 2;
        }

        String treePlantingTime = newTree.getPlantingTime();

        if (treePlantingTime.equals("morning")) {
            addedPoints += 2;
        } else if (treePlantingTime.equals("afternoon")) {
            addedPoints += 1;
        }

        String treeLocation = newTree.getTreeLocation();

        if (treeLocation.equals("Al Ain")) {
            addedPoints += 3;
        } else if (treeLocation.equals("Dubai") || treeLocation.equals("Abu Dhabi")) {
            addedPoints += 2;
        } else if (treeLocation.equals("Sharjah")) {
            addedPoints += 1;
        }

        user.setPoints(userPreviousPoints + addedPoints);

        RewardHistory newRecord = new RewardHistory();
        newRecord.setTreeId(newTree.getTreeId());
        newRecord.setUserId(user.getUserId());

        rewardHistoryRepository.save(newRecord);

        userService.saveUser(user);


    }

    @Override
    public List<RewardHistory> getUserRewardsHistory(Long userId) {

        // Do we have this data inside Cache ? yes -> return it, no -> continue the code flow.

        if (userRewardHistory.containsKey(userId)) {
            System.out.println("Loading user reward history from Cache..");
            return userRewardHistory.get(userId);
        }

        System.out.println("Loading user reward history from MySQL Database..");

        if (userRewardHistory.containsKey(userId)) {
            return userRewardHistory.get(userId);
        }

        List<RewardHistory> rewardHistories = rewardHistoryRepository.getUserRewardsHistory(userId); // List of Tree Entity (With Image)

        userRewardHistory.put(userId, rewardHistories);

        return rewardHistories;
    }

}
