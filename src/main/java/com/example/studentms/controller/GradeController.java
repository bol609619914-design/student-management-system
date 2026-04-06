package com.example.studentms.controller;

import com.example.studentms.model.AppUser;
import com.example.studentms.model.UserRole;
import com.example.studentms.service.CourseService;
import com.example.studentms.service.GradeService;
import com.example.studentms.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class GradeController {

    private final GradeService gradeService;
    private final CourseService courseService;
    private final StudentService studentService;

    public GradeController(GradeService gradeService, CourseService courseService, StudentService studentService) {
        this.gradeService = gradeService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping("/grades")
    public String grades(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() == UserRole.STUDENT) {
            Long studentId = studentService.findByUserId(currentUser.getId()).getId();
            model.addAttribute("grades", gradeService.findGradesByStudent(studentId));
            model.addAttribute("myGpa", gradeService.calculateStudentAverageGpa(studentId));
        } else {
            model.addAttribute("grades", gradeService.findAllGrades());
            model.addAttribute("ranking", gradeService.buildRanking());
            model.addAttribute("allEnrollments", courseService.findAllEnrollments());
        }
        model.addAttribute("allGrades", gradeService.findAllGrades());
        return "grades";
    }

    @PostMapping("/grades")
    public String saveGrade(@RequestParam Long enrollmentId,
                            @RequestParam Double dailyScore,
                            @RequestParam Double midtermScore,
                            @RequestParam Double finalScore,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() == UserRole.STUDENT) {
            return "redirect:/grades";
        }
        gradeService.saveGrade(enrollmentId, dailyScore, midtermScore, finalScore);
        redirectAttributes.addFlashAttribute("successMessage", "成绩已录入");
        return "redirect:/grades";
    }

    @GetMapping("/grades/export")
    public void export(HttpSession session, HttpServletResponse response) throws IOException {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        Long studentId = currentUser.getRole() == UserRole.STUDENT
                ? studentService.findByUserId(currentUser.getId()).getId()
                : null;

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=grades.csv");

        StringBuilder csv = new StringBuilder("学生,课程,平时,期中,期末,总评,GPA\n");
        gradeService.findAllGrades().stream()
                .filter(item -> studentId == null || item.getEnrollment().getStudent().getId().equals(studentId))
                .forEach(item -> csv.append(item.getEnrollment().getStudent().getName()).append(',')
                        .append(item.getEnrollment().getCourse().getCourseName()).append(',')
                        .append(item.getDailyScore()).append(',')
                        .append(item.getMidtermScore()).append(',')
                        .append(item.getFinalScore()).append(',')
                        .append(item.getTotalScore()).append(',')
                        .append(item.getGpa()).append('\n'));
        response.getWriter().write(csv.toString());
    }
}
