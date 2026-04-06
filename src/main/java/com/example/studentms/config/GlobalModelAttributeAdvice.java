package com.example.studentms.config;

import com.example.studentms.model.AppUser;
import com.example.studentms.service.AnnouncementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    private final AnnouncementService announcementService;

    public GlobalModelAttributeAdvice(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @ModelAttribute("currentUser")
    public AppUser currentUser(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof AppUser appUser) {
            return appUser;
        }
        return null;
    }

    @ModelAttribute("unreadAnnouncementCount")
    public long unreadAnnouncementCount(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser instanceof AppUser appUser) {
            return announcementService.unreadCount(appUser);
        }
        return 0;
    }
}
