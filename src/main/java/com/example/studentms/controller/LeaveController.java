package com.example.studentms.controller;

import com.example.studentms.model.AppUser;
import com.example.studentms.model.UserRole;
import com.example.studentms.service.LeaveService;
import com.example.studentms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class LeaveController {

    private final LeaveService leaveService;
    private final StudentService studentService;

    public LeaveController(LeaveService leaveService, StudentService studentService) {
        this.leaveService = leaveService;
        this.studentService = studentService;
    }

    @GetMapping("/leaves")
    public String leaves(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() == UserRole.STUDENT) {
            model.addAttribute("leaveRequests", leaveService.findStudentLeaves(studentService.findByUserId(currentUser.getId()).getId()));
        } else {
            model.addAttribute("leaveRequests", leaveService.findAll());
        }
        return "leaves";
    }

    @PostMapping("/leaves")
    public String submit(@RequestParam String reason,
                         @RequestParam LocalDate startDate,
                         @RequestParam LocalDate endDate,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() != UserRole.STUDENT) {
            return "redirect:/leaves";
        }
        leaveService.submit(studentService.findByUserId(currentUser.getId()), reason, startDate, endDate);
        redirectAttributes.addFlashAttribute("successMessage", "请假申请已提交");
        return "redirect:/leaves";
    }

    @GetMapping("/leaves/{id}/approve")
    public String approve(@PathVariable Long id,
                          @RequestParam(defaultValue = "true") boolean approved,
                          @RequestParam(defaultValue = "已处理") String comment,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() == UserRole.STUDENT) {
            return "redirect:/leaves";
        }
        leaveService.approve(id, currentUser, approved, comment);
        redirectAttributes.addFlashAttribute("successMessage", approved ? "请假申请已通过" : "请假申请已驳回");
        return "redirect:/leaves";
    }
}
