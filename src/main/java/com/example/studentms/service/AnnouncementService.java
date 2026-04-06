package com.example.studentms.service;

import com.example.studentms.model.Announcement;
import com.example.studentms.model.AnnouncementRead;
import com.example.studentms.model.AppUser;
import com.example.studentms.model.UserRole;
import com.example.studentms.repository.AppUserRepository;
import com.example.studentms.repository.AnnouncementReadRepository;
import com.example.studentms.repository.AnnouncementRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementReadRepository announcementReadRepository;
    private final AppUserRepository appUserRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository,
                               AnnouncementReadRepository announcementReadRepository,
                               AppUserRepository appUserRepository) {
        this.announcementRepository = announcementRepository;
        this.announcementReadRepository = announcementReadRepository;
        this.appUserRepository = appUserRepository;
    }

    @PostConstruct
    public void initAnnouncements() {
        if (announcementRepository.count() > 0) {
            return;
        }
        appUserRepository.findByRole(UserRole.ADMIN).stream().findFirst().ifPresent(admin ->
                publish("期中考试安排通知", "请各位同学于下周二上午八点前到指定教室参加期中考试。", true, admin)
        );
    }

    public List<Announcement> findAll() {
        return announcementRepository.findAllByOrderByPinnedDescPublishedAtDesc();
    }

    public void publish(String title, String content, boolean pinned, AppUser publisher) {
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setPinned(pinned);
        announcement.setPublishedAt(LocalDateTime.now());
        announcement.setPublisher(publisher);
        announcementRepository.save(announcement);
    }

    public long unreadCount(AppUser user) {
        long total = announcementRepository.count();
        long read = announcementReadRepository.countByUserId(user.getId());
        return Math.max(total - read, 0);
    }

    public void markAllAsRead(AppUser user) {
        for (Announcement announcement : announcementRepository.findAll()) {
            if (!announcementReadRepository.existsByAnnouncementIdAndUserId(announcement.getId(), user.getId())) {
                AnnouncementRead read = new AnnouncementRead();
                read.setAnnouncement(announcement);
                read.setUser(user);
                read.setReadAt(LocalDateTime.now());
                announcementReadRepository.save(read);
            }
        }
    }
}
