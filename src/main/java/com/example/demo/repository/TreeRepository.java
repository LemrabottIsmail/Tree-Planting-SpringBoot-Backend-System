package com.example.demo.repository;


import com.example.demo.model.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreeRepository extends JpaRepository<Tree, Long> {

    // Custom Query for retrieving user planting history.
    @Query("SELECT t FROM Tree t WHERE t.userId = ?1")
    List<Tree> getUserPlantingHistory(Long userId);

}
