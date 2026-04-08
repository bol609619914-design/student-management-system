package com.example.studentms.service;

import com.example.studentms.dto.LoginForm;
import com.example.studentms.dto.ProfileForm;
import com.example.studentms.dto.RegisterForm;
import com.example.studentms.model.AppUser;
import com.example.studentms.model.Student;
import com.example.studentms.model.UserRole;
import com.example.studentms.repository.AppUserRepository;
import com.example.studentms.repository.StudentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(AppUserRepository appUserRepository, StudentRepository studentRepository) {
        this.appUserRepository = appUserRepository;
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    public void initUsers() {
        AppUser admin = ensureUser("admin", "admin123", "系统管理员", UserRole.ADMIN, "13800001000");
        AppUser teacher = ensureUser("teacher", "teacher123", "张老师", UserRole.TEACHER, "13800002000");
        AppUser studentUser = ensureUser("student", "student123", "王小明", UserRole.STUDENT, "13800003000");

        studentRepository.findByUserId(studentUser.getId()).orElseGet(() -> {
            Student student = new Student();
            student.setStudentNo("20260001");
            student.setName("王小明");
            student.setAge(20);
            student.setGender("男");
            student.setClassroom("计算机科学与技术 1 班");
            student.setPhone("13800003000");
            student.setMajor("计算机科学与技术");
            student.setEnrollmentDate(LocalDate.of(2026, 9, 1));
            student.setCurrentStatus("在读");
            student.setPhotoUrl("https://images.unsplash.com/photo-1500648767791-00dcc994a43e");
            student.setUser(studentUser);
            return studentRepository.save(student);
        });

        if (admin.getAvatarUrl() == null) {
            admin.setAvatarUrl("https://images.unsplash.com/photo-1560250097-0b93528c311a");
            appUserRepository.save(admin);
        }
        if (teacher.getAvatarUrl() == null) {
            teacher.setAvatarUrl("https://images.unsplash.com/photo-1544717305-2782549b5136");
            appUserRepository.save(teacher);
        }
    }

    private AppUser ensureUser(String username, String password, String fullName, UserRole role, String phone) {
        AppUser user = appUserRepository.findByUsername(username).orElseGet(AppUser::new);
        user.setUsername(username);
        user.setFullName(fullName);
        user.setRole(role);
        user.setPhone(phone);
        user.setEnabled(Boolean.TRUE);
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password));
        }
        return appUserRepository.save(user);
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    public boolean usernameExists(String username) {
        return appUserRepository.existsByUsername(username);
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    public AppUser createManagedUser(String fullName, String username, String password, String phone, UserRole role) {
        if (usernameExists(username)) {
            throw new IllegalArgumentException("该用户名已被注册");
        }
        if (password == null || password.isBlank() || password.length() < 6) {
            throw new IllegalArgumentException("初始密码长度不能少于 6 位");
        }

        AppUser user = new AppUser();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPhone(phone == null ? "" : phone.trim());
        user.setRole(role);
        user.setEnabled(Boolean.TRUE);
        user.setPassword(passwordEncoder.encode(password));
        AppUser savedUser = appUserRepository.save(user);

        if (role == UserRole.STUDENT) {
            Student student = new Student();
            student.setStudentNo("S" + System.currentTimeMillis());
            student.setName(fullName);
            student.setAge(18);
            student.setGender("男");
            student.setClassroom("待分班");
            student.setPhone((phone == null || phone.isBlank()) ? "未填写" : phone.trim());
            student.setMajor("待分配专业");
            student.setEnrollmentDate(LocalDate.now());
            student.setCurrentStatus("在读");
            student.setUser(savedUser);
            studentRepository.save(student);
        }

        return savedUser;
    }

    public AppUser register(RegisterForm form) {
        if (usernameExists(form.getUsername())) {
            throw new IllegalArgumentException("该用户名已被注册");
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }

        String phone = form.getPhone() == null ? "" : form.getPhone().trim();

        AppUser user = new AppUser();
        user.setFullName(form.getFullName());
        user.setUsername(form.getUsername());
        user.setPhone(phone);
        user.setRole(UserRole.STUDENT);
        user.setEnabled(Boolean.TRUE);
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        AppUser savedUser = appUserRepository.save(user);

        Student student = new Student();
        student.setStudentNo("S" + System.currentTimeMillis());
        student.setName(form.getFullName());
        student.setAge(18);
        student.setGender("男");
        student.setClassroom("待分班");
        student.setPhone(phone.isBlank() ? "未填写" : phone);
        student.setMajor("待分配专业");
        student.setEnrollmentDate(LocalDate.now());
        student.setCurrentStatus("在读");
        student.setUser(savedUser);
        studentRepository.save(student);
        return savedUser;
    }

    public AppUser login(LoginForm form) {
        AppUser user = appUserRepository.findByUsername(form.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));

        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (!user.getEnabled()) {
            throw new IllegalArgumentException("该账号已被禁用");
        }
        return user;
    }

    public void updateRole(Long userId, UserRole role) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setRole(role);
        appUserRepository.save(user);
    }

    public void updateProfile(AppUser currentUser, ProfileForm form) {
        AppUser user = appUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setFullName(form.getFullName());
        user.setPhone(form.getPhone());
        user.setAvatarUrl(form.getAvatarUrl());

        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            if (form.getOldPassword() == null || !passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("原密码不正确");
            }
            if (!form.getNewPassword().equals(form.getConfirmPassword())) {
                throw new IllegalArgumentException("两次输入的新密码不一致");
            }
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }
        appUserRepository.save(user);
    }
}
