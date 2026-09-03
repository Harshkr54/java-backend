package com.storvix.backend.repository;

import com.storvix.backend.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, String> {
    List<Folder> findByOwnerIdAndParentFolderIdAndIsDeletedFalseOrderByName(String ownerId, String parentFolderId);
    List<Folder> findByParentFolderIdAndIsDeletedFalseOrderByName(String parentFolderId);
    List<Folder> findByOwnerIdAndParentFolderIsNullAndIsDeletedFalseOrderByName(String ownerId);
    boolean existsByOwnerIdAndParentFolderIdAndNameAndIsDeletedFalse(String ownerId, String parentFolderId, String name);
    boolean existsByOwnerIdAndParentFolderIsNullAndNameAndIsDeletedFalse(String ownerId, String name);
    List<Folder> findByOwnerIdAndIsDeletedTrue(String ownerId);
    long countByOwnerIdAndIsDeletedFalse(String ownerId);
}
