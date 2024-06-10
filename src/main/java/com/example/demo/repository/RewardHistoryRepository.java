package com.example.demo.repository;

import com.example.demo.model.RewardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {
    @Query("SELECT r FROM RewardHistory r WHERE r.userId = ?1")
    List<RewardHistory> getUserRewardsHistory(Long userId);
}
