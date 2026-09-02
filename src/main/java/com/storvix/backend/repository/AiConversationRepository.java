package com.storvix.backend.repository;

import com.storvix.backend.entity.AiConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AiConversationRepository extends JpaRepository<AiConversation, String> {
    Optional<AiConversation> findByUserIdAndFileId(String userId, String fileId);
}
