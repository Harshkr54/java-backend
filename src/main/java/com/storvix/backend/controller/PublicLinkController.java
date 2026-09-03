package com.storvix.backend.controller;

import com.storvix.backend.dto.ApiResponse;
import com.storvix.backend.entity.File;
import com.storvix.backend.entity.Folder;
import com.storvix.backend.entity.PublicLink;
import com.storvix.backend.repository.FileRepository;
import com.storvix.backend.repository.FolderRepository;
import com.storvix.backend.repository.PublicLinkRepository;
import com.storvix.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/public-links")
@RequiredArgsConstructor
public class PublicLinkController {

    private final PublicLinkRepository publicLinkRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<PublicLink>> createPublicLink(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, Object> request) {
        
        String fileId = (String) request.get("fileId");
        String folderId = (String) request.get("folderId");
        String password = (String) request.get("password");
        
        PublicLink link = new PublicLink();
        link.setCreatedBy(userDetails.getUser());
        link.setToken(UUID.randomUUID().toString());
        link.setIsActive(true);
        link.setCreatedAt(LocalDateTime.now());
        link.setUpdatedAt(LocalDateTime.now());
        
        if (password != null && !password.isEmpty()) {
            link.setPasswordHash(password); // Simplified for MVP, should be hashed
        }

        String expiresAtStr = (String) request.get("expiresAt");
        if (expiresAtStr != null && !expiresAtStr.isEmpty()) {
            link.setExpiresAt(LocalDateTime.parse(expiresAtStr));
        }
        
        if (fileId != null) {
            File file = fileRepository.findById(fileId).orElseThrow();
            link.setFile(file);
        } else if (folderId != null) {
            Folder folder = folderRepository.findById(folderId).orElseThrow();
            link.setFolder(folder);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail("Must provide fileId or folderId", "BAD_REQUEST"));
        }
        
        PublicLink saved = publicLinkRepository.save(link);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Public link created", saved));
    }
}
