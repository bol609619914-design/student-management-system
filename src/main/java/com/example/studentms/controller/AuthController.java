package com.example.studentms.controller;

import com.example.studentms.dto.LoginForm;
import com.example.studentms.dto.RegisterForm;
import com.example.studentms.model.AppUser;
import com.example.studentms.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("loginUser") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            AppUser user = authService.login(loginForm);
            session.setAttribute("loginUser", user);
            return "redirect:/dashboard";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("authError", ex.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model) {
        if (session.getAttribute("loginUser") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm registerForm,
                           BindingResult bindingResult,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            AppUser user = authService.register(registerForm);
            session.setAttribute("loginUser", user);
            redirectAttributes.addFlashAttribute("successMessage", "注册成功，欢迎进入系统");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("authError", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "您已退出登录");
        return "redirect:/login";
    }
}
