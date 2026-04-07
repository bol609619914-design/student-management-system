package com.example.studentms.controller;

import com.example.studentms.model.AppUser;
import com.example.studentms.model.Student;
import com.example.studentms.service.DashboardService;
import com.example.studentms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final StudentService studentService;

    public DashboardController(DashboardService dashboardService, StudentService studentService) {
        this.dashboardService = dashboardService;
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        if ("STUDENT".equals(currentUser.getRole().name())) {
            Student student = studentService.findOrCreateByUserId(currentUser.getId());
            model.addAttribute("studentDashboard", dashboardService.buildStudentDashboard(currentUser, student));
            return "dashboard-student";
        }

        if ("TEACHER".equals(currentUser.getRole().name())) {
            model.addAttribute("teacherDashboard", dashboardService.buildTeacherDashboard(currentUser));
            return "dashboard-teacher";
        }

        model.addAttribute("stats", dashboardService.buildStats());
        model.addAttribute("ranking", dashboardService.ranking());
        model.addAttribute("scheduleHeatMap", dashboardService.buildScheduleHeatMap());
        model.addAttribute("weekdayLoad", dashboardService.buildWeekdayCourseLoad());
        model.addAttribute("maxWeekdayLoad", dashboardService.maxWeekdayCourseLoad());
        model.addAttribute("topCourses", dashboardService.topCourses());
        model.addAttribute("recentAnnouncements", dashboardService.recentAnnouncements());
        model.addAttribute("recentLeaves", dashboardService.recentLeaves());
        return "dashboard";
    }
}
