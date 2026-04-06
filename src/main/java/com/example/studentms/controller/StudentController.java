package com.example.studentms.controller;

import com.example.studentms.model.AppUser;
import com.example.studentms.model.Student;
import com.example.studentms.model.StudentStatusType;
import com.example.studentms.model.UserRole;
import com.example.studentms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String listStudents(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        List<Student> students = currentUser.getRole() == UserRole.STUDENT
                ? List.of(studentService.findByUserId(currentUser.getId()))
                : studentService.findAll();
        model.addAttribute("students", students.stream().filter(item -> item != null).toList());
        model.addAttribute("studentCount", studentService.count());
        model.addAttribute("averageAge", String.format("%.1f", studentService.averageAge()));
        return "students";
    }

    @GetMapping("/students/new")
    public String showCreateForm(HttpSession session, Model model) {
        if (((AppUser) session.getAttribute("loginUser")).getRole() == UserRole.STUDENT) {
            return "redirect:/students";
        }
        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "新增学生");
        model.addAttribute("formAction", "/students");
        return "student-form";
    }

    @PostMapping("/students")
    public String createStudent(@Valid @ModelAttribute("student") Student student,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (((AppUser) session.getAttribute("loginUser")).getRole() == UserRole.STUDENT) {
            return "redirect:/students";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "新增学生");
            model.addAttribute("formAction", "/students");
            return "student-form";
        }

        studentService.save(student);
        redirectAttributes.addFlashAttribute("successMessage", "学生信息新增成功");
        return "redirect:/students";
    }

    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (((AppUser) session.getAttribute("loginUser")).getRole() == UserRole.STUDENT) {
            return "redirect:/students";
        }
        Student student = studentService.findById(id);
        if (student == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "未找到对应的学生信息");
            return "redirect:/students";
        }

        model.addAttribute("student", student);
        model.addAttribute("pageTitle", "编辑学生");
        model.addAttribute("formAction", "/students/update");
        return "student-form";
    }

    @PostMapping("/students/update")
    public String updateStudent(@Valid @ModelAttribute("student") Student student,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (((AppUser) session.getAttribute("loginUser")).getRole() == UserRole.STUDENT) {
            return "redirect:/students";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "编辑学生");
            model.addAttribute("formAction", "/students/update");
            return "student-form";
        }

        studentService.update(student);
        redirectAttributes.addFlashAttribute("successMessage", "学生信息修改成功");
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (((AppUser) session.getAttribute("loginUser")).getRole() == UserRole.STUDENT) {
            return "redirect:/students";
        }
        studentService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "学生信息删除成功");
        return "redirect:/students";
    }

    @GetMapping("/students/{id}/status")
    public String statusPage(@PathVariable Long id, HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() == UserRole.STUDENT) {
            Student self = studentService.findByUserId(currentUser.getId());
            if (self == null || !self.getId().equals(id)) {
                return "redirect:/students";
            }
        }
        model.addAttribute("student", studentService.findById(id));
        model.addAttribute("history", studentService.getStatusHistory(id));
        model.addAttribute("statusTypes", StudentStatusType.values());
        return "student-status";
    }

    @PostMapping("/students/{id}/status")
    public String addStatus(@PathVariable Long id,
                            @RequestParam StudentStatusType changeType,
                            @RequestParam String afterStatus,
                            @RequestParam String reason,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (((AppUser) session.getAttribute("loginUser")).getRole() == UserRole.STUDENT) {
            return "redirect:/students";
        }
        studentService.addStatusChange(id, changeType, afterStatus, reason);
        redirectAttributes.addFlashAttribute("successMessage", "学籍异动记录已保存");
        return "redirect:/students/" + id + "/status";
    }
}
