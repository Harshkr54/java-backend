package com.storvix.backend.service;

import com.storvix.backend.dto.DashboardResponse;
import com.storvix.backend.entity.User;
import com.storvix.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final StarRepository starRepository;
    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboardData(String userId) {
        User user = userRepository.findById(userId).orElseThrow();

        Map<String, Object> storage = new HashMap<>();
        storage.put("used", user.getStorageUsed());
        storage.put("quota", user.getStorageQuota());

        Map<String, Object> totals = new HashMap<>();
        totals.put("files", fileRepository.countByOwnerIdAndIsDeletedFalse(userId));
        totals.put("folders", folderRepository.countByOwnerIdAndIsDeletedFalse(userId));
        totals.put("starred", starRepository.countByUserId(userId));

        return DashboardResponse.builder()
                .storage(storage)
                .totals(totals)
                .recentFiles(fileRepository.findTop5ByOwnerIdAndIsDeletedFalseOrderByCreatedAtDesc(userId).stream().map(f -> (Object) f).collect(Collectors.toList()))
                .starred(starRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(s -> (Object) s).collect(Collectors.toList()))
                .recentActivity(activityRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(a -> (Object) a).collect(Collectors.toList()))
                .build();
    }
}
