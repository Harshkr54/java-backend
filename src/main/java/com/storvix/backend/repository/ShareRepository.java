package com.storvix.backend.repository;

import com.storvix.backend.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShareRepository extends JpaRepository<Share, String> {
    List<Share> findBySharedWithId(String sharedWithId);
}
