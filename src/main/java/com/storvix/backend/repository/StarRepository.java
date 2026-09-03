package com.storvix.backend.repository;

import com.storvix.backend.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<Star, String> {
    List<Star> findByUserIdOrderByCreatedAtDesc(String userId);
    long countByUserId(String userId);
}
