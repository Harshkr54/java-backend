package com.storvix.backend.controller;

import com.storvix.backend.dto.ApiResponse;
import com.storvix.backend.dto.FileResponse;
import com.storvix.backend.dto.InitUploadRequest;
import com.storvix.backend.security.CustomUserDetails;
import com.storvix.backend.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload/init")
    public ResponseEntity<ApiResponse<Map<String, Object>>> initUpload(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody InitUploadRequest request) {
        Map<String, Object> data = fileService.initUpload(userDetails.getUser().getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Upload initialized", data));
    }

    @PostMapping("/upload/complete")
    public ResponseEntity<ApiResponse<FileResponse>> completeUpload(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> body) {
        String fileId = body.get("fileId");
        FileResponse file = fileService.completeUpload(userDetails.getUser().getId(), fileId);
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", file));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<ApiResponse<Map<String, String>>> getDownloadUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String id) {
        Map<String, String> data = fileService.getDownloadUrl(userDetails.getUser().getId(), id);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
