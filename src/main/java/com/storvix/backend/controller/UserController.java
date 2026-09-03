package com.storvix.backend.controller;

import com.storvix.backend.dto.ApiResponse;
import com.storvix.backend.entity.User;
import com.storvix.backend.repository.UserRepository;
import com.storvix.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me/storage")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStorageInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        
        Map<String, Object> storageData = new HashMap<>();
        // AppShell expects these
        storageData.put("storageUsed", user.getStorageUsed());
        storageData.put("storageQuota", user.getStorageQuota());
        // User prompt explicitly asked for these
        storageData.put("used", user.getStorageUsed());
        storageData.put("total", user.getStorageQuota());
        
        double percentage = 0.0;
        if (user.getStorageQuota() != null && user.getStorageQuota() > 0) {
            percentage = ((double) user.getStorageUsed() / user.getStorageQuota()) * 100;
        }
        storageData.put("percentage", percentage);

        return ResponseEntity.ok(ApiResponse.success(storageData));
    }
}
