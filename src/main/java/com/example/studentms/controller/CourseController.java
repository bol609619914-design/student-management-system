package com.example.studentms.controller;

import com.example.studentms.model.AppUser;
import com.example.studentms.model.Course;
import com.example.studentms.model.CourseNature;
import com.example.studentms.model.Student;
import com.example.studentms.model.UserRole;
import com.example.studentms.service.AuthService;
import com.example.studentms.service.CourseService;
import com.example.studentms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final AuthService authService;
    private final StudentService studentService;

    public CourseController(CourseService courseService, AuthService authService, StudentService studentService) {
        this.courseService = courseService;
        this.authService = authService;
        this.studentService = studentService;
    }

    @GetMapping("/courses")
    public String coursePage(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        model.addAttribute("courses", currentUser.getRole() == UserRole.TEACHER
                ? courseService.findCoursesForTeacher(currentUser.getId())
                : courseService.findAllCourses());
        model.addAttribute("teachers", authService.findAllUsers().stream()
                .filter(user -> user.getRole() == UserRole.TEACHER)
                .toList());
        model.addAttribute("natures", CourseNature.values());
        if (currentUser.getRole() == UserRole.STUDENT) {
            Student student = studentService.findByUserId(currentUser.getId());
            model.addAttribute("myCourses", courseService.findEnrollmentsByStudent(student.getId()));
        }
        return "courses";
    }

    @PostMapping("/courses")
    public String saveCourse(@RequestParam(required = false) Long id,
                             @RequestParam String courseCode,
                             @RequestParam String courseName,
                             @RequestParam Integer credit,
                             @RequestParam Integer classHours,
                             @RequestParam CourseNature nature,
                             @RequestParam Long teacherId,
                             @RequestParam String classroom,
                             @RequestParam String weekDay,
                             @RequestParam Integer startSection,
                             @RequestParam Integer endSection,
                             @RequestParam Integer capacity,
                             @RequestParam String description,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() == UserRole.STUDENT) {
            return "redirect:/courses";
        }

        try {
            Course course = id == null ? new Course() : courseService.findCourse(id);
            if (course == null) {
                course = new Course();
            }
            course.setCourseCode(courseCode);
            course.setCourseName(courseName);
            course.setCredit(credit);
            course.setClassHours(classHours);
            course.setNature(nature);
            course.setTeacher(authService.findById(teacherId));
            course.setClassroom(classroom);
            course.setWeekDay(weekDay);
            course.setStartSection(startSection);
            course.setEndSection(endSection);
            course.setCapacity(capacity);
            course.setDescription(description);
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "课程保存成功");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id, HttpSession session) {
        if (((AppUser) session.getAttribute("loginUser")).getRole() == UserRole.STUDENT) {
            return "redirect:/courses";
        }
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/select")
    public String selectCourse(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() != UserRole.STUDENT) {
            return "redirect:/courses";
        }
        try {
            Student student = studentService.findByUserId(currentUser.getId());
            courseService.enroll(student, id);
            redirectAttributes.addFlashAttribute("successMessage", "选课成功");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/courses";
    }
}
