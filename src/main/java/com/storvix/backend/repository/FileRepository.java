package com.storvix.backend.repository;

import com.storvix.backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {
    List<File> findByFolderIdAndIsDeletedFalseAndUploadStatusOrderByName(String folderId, String uploadStatus);
    List<File> findByOwnerIdAndFolderIsNullAndIsDeletedFalseAndUploadStatusOrderByName(String ownerId, String uploadStatus);
    List<File> findByOwnerIdAndIsDeletedTrue(String ownerId);
    List<File> findByOwnerIdAndIsDeletedFalseAndUploadStatus(String ownerId, String uploadStatus);
    long countByOwnerIdAndIsDeletedFalse(String ownerId);
    long countByOwnerIdAndIsDeletedFalseAndUploadStatus(String ownerId, String uploadStatus);
    List<File> findTop5ByOwnerIdAndIsDeletedFalseOrderByCreatedAtDesc(String ownerId);
}
