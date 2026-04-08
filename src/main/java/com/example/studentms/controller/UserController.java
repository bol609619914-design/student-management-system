package com.example.studentms.controller;

import com.example.studentms.dto.ProfileForm;
import com.example.studentms.model.AppUser;
import com.example.studentms.model.UserRole;
import com.example.studentms.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        ProfileForm form = new ProfileForm();
        form.setFullName(currentUser.getFullName());
        form.setPhone(currentUser.getPhone());
        form.setAvatarUrl(currentUser.getAvatarUrl());
        model.addAttribute("profileForm", form);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute ProfileForm profileForm,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        try {
            authService.updateProfile(currentUser, profileForm);
            AppUser refreshed = authService.findById(currentUser.getId());
            session.setAttribute("loginUser", refreshed);
            redirectAttributes.addFlashAttribute("successMessage", "个人信息更新成功");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/profile";
    }

    @GetMapping("/users")
    public String userManage(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() != UserRole.ADMIN) {
            return "redirect:/dashboard";
        }
        model.addAttribute("users", authService.findAllUsers());
        model.addAttribute("roles", UserRole.values());
        return "users";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("fullName") String fullName,
                             @ModelAttribute("username") String username,
                             @ModelAttribute("password") String password,
                             @ModelAttribute("phone") String phone,
                             @ModelAttribute("role") UserRole role,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() != UserRole.ADMIN) {
            return "redirect:/dashboard";
        }
        try {
            authService.createManagedUser(fullName, username, password, phone, role);
            redirectAttributes.addFlashAttribute("successMessage", "账号创建成功");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/role")
    public String updateRole(@PathVariable Long id,
                             @ModelAttribute("role") UserRole role,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        AppUser currentUser = (AppUser) session.getAttribute("loginUser");
        if (currentUser.getRole() != UserRole.ADMIN) {
            return "redirect:/dashboard";
        }
        authService.updateRole(id, role);
        redirectAttributes.addFlashAttribute("successMessage", "角色更新成功");
        return "redirect:/users";
    }
}
