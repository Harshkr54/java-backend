package com.storvix.backend.dto;

import com.storvix.backend.entity.Folder;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class FolderResponse {
    private String id;
    private String name;
    private String ownerId;
    private String parentFolderId;
    private String path;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    @com.fasterxml.jackson.annotation.JsonProperty("_id")
    public String get_id() {
        return id;
    }

    public static FolderResponse from(Folder folder) {
        return FolderResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .ownerId(folder.getOwner().getId())
                .parentFolderId(folder.getParentFolder() != null ? folder.getParentFolder().getId() : null)
                .path(folder.getPath())
                .createdAt(folder.getCreatedAt())
                .updatedAt(folder.getUpdatedAt())
                .isDeleted(folder.getIsDeleted())
                .build();
    }
}
