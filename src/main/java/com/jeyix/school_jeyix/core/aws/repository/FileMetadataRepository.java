package com.jeyix.school_jeyix.core.aws.repository;

import com.jeyix.school_jeyix.core.aws.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository  extends JpaRepository<FileMetadata, Long> {
    FileMetadata findByKey(String key);
}
