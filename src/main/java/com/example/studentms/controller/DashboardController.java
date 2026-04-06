package com.example.studentms.controller;

import com.example.studentms.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
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
