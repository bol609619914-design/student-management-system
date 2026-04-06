package com.example.studentms.controller;

import com.example.studentms.model.AppUser;
import com.example.studentms.model.UserRole;
import com.example.studentms.service.AnnouncementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/announcements")
    public String announcements(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        announcementService.markAllAsRead(currentUser);
        model.addAttribute("announcements", announcementService.findAll());
        return "announcements";
    }

    @PostMapping("/announcements")
    public String publish(@RequestParam String title,
                          @RequestParam String content,
                          @RequestParam(defaultValue = "false") boolean pinned,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() == UserRole.STUDENT) {
            return "redirect:/announcements";
        }
        announcementService.publish(title, content, pinned, currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "公告发布成功");
        return "redirect:/announcements";
    }
}
