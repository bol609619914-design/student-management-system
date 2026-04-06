package com.example.studentms.repository;

import com.example.studentms.model.AnnouncementRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementReadRepository extends JpaRepository<AnnouncementRead, Long> {

    boolean existsByAnnouncementIdAndUserId(Long announcementId, Long userId);

    long countByUserId(Long userId);
}
