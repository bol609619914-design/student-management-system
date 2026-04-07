package com.example.studentms.config;

import com.example.studentms.model.Announcement;
import com.example.studentms.model.AppUser;
import com.example.studentms.model.Course;
import com.example.studentms.model.CourseNature;
import com.example.studentms.model.Enrollment;
import com.example.studentms.model.GradeRecord;
import com.example.studentms.model.LeaveRequest;
import com.example.studentms.model.LeaveStatus;
import com.example.studentms.model.Student;
import com.example.studentms.model.StudentStatusChange;
import com.example.studentms.model.StudentStatusType;
import com.example.studentms.model.UserRole;
import com.example.studentms.repository.AnnouncementRepository;
import com.example.studentms.repository.AppUserRepository;
import com.example.studentms.repository.CourseRepository;
import com.example.studentms.repository.EnrollmentRepository;
import com.example.studentms.repository.GradeRecordRepository;
import com.example.studentms.repository.LeaveRequestRepository;
import com.example.studentms.repository.StudentRepository;
import com.example.studentms.repository.StudentStatusChangeRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("demo")
public class DemoDataInitializer implements ApplicationRunner {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRecordRepository gradeRecordRepository;
    private final AnnouncementRepository announcementRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final StudentStatusChangeRepository studentStatusChangeRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DemoDataInitializer(AppUserRepository appUserRepository,
                               StudentRepository studentRepository,
                               CourseRepository courseRepository,
                               EnrollmentRepository enrollmentRepository,
                               GradeRecordRepository gradeRecordRepository,
                               AnnouncementRepository announcementRepository,
                               LeaveRequestRepository leaveRequestRepository,
                               StudentStatusChangeRepository studentStatusChangeRepository) {
        this.appUserRepository = appUserRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.gradeRecordRepository = gradeRecordRepository;
        this.announcementRepository = announcementRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.studentStatusChangeRepository = studentStatusChangeRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (appUserRepository.count() > 3 && courseRepository.count() > 0) {
            return;
        }

        AppUser admin = upsertUser("admin", "admin123", "系统管理员", UserRole.ADMIN, "13800001000",
                "https://images.unsplash.com/photo-1560250097-0b93528c311a");
        AppUser teacher = upsertUser("teacher", "teacher123", "张老师", UserRole.TEACHER, "13800002000",
                "https://images.unsplash.com/photo-1544717305-2782549b5136");
        AppUser teacher2 = upsertUser("teacher2", "teacher123", "李老师", UserRole.TEACHER, "13800002001",
                "https://images.unsplash.com/photo-1544005313-94ddf0286df2");
        AppUser teacher3 = upsertUser("teacher3", "teacher123", "陈老师", UserRole.TEACHER, "13800002002",
                "https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91");
        AppUser studentUser1 = upsertUser("student", "student123", "王小明", UserRole.STUDENT, "13800003000",
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e");
        AppUser studentUser2 = upsertUser("student2", "student123", "赵欣怡", UserRole.STUDENT, "13800003001",
                "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df");
        AppUser studentUser3 = upsertUser("student3", "student123", "孙浩然", UserRole.STUDENT, "13800003002",
                "https://images.unsplash.com/photo-1504593811423-6dd665756598");
        AppUser studentUser4 = upsertUser("student4", "student123", "周雨桐", UserRole.STUDENT, "13800003003",
                "https://images.unsplash.com/photo-1438761681033-6461ffad8d80");

        Student student1 = upsertStudent(studentUser1, "20260001", "王小明", 20, "男", "计算机科学与技术1班",
                "13800003000", "计算机科学与技术", LocalDate.of(2026, 9, 1), "在读",
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e");
        Student student2 = upsertStudent(studentUser2, "20260005", "赵欣怡", 19, "女", "软件工程2班",
                "13800003001", "软件工程", LocalDate.of(2026, 9, 1), "在读",
                "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df");
        Student student3 = upsertStudent(studentUser3, "20260006", "孙浩然", 20, "男", "计算机科学与技术1班",
                "13800003002", "计算机科学与技术", LocalDate.of(2025, 9, 1), "在读",
                "https://images.unsplash.com/photo-1504593811423-6dd665756598");
        Student student4 = upsertStudent(studentUser4, "20260007", "周雨桐", 18, "女", "数据科学1班",
                "13800003003", "数据科学与大数据技术", LocalDate.of(2026, 9, 1), "在读",
                "https://images.unsplash.com/photo-1438761681033-6461ffad8d80");

        Student student5 = upsertStandaloneStudent("20260002", "张三", 19, "男", "软件工程1班", "13800000001",
                "软件工程", LocalDate.of(2026, 9, 1), "在读",
                "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d");
        Student student6 = upsertStandaloneStudent("20260003", "李思雨", 20, "女", "计算机科学与技术2班", "13800000002",
                "计算机科学与技术", LocalDate.of(2026, 9, 1), "在读",
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330");
        Student student7 = upsertStandaloneStudent("20260004", "王志强", 21, "男", "网络工程1班", "13800000003",
                "网络工程", LocalDate.of(2025, 9, 1), "在读",
                "https://images.unsplash.com/photo-1568602471122-7832951cc4c5");

        Course cs301 = upsertCourse("CS301", "Java Web开发", 4, 64, CourseNature.REQUIRED, teacher,
                "A101", "周一", 1, 2, 60, "Spring Boot 与 Web 应用开发实践");
        Course cs302 = upsertCourse("CS302", "数据库原理", 3, 48, CourseNature.REQUIRED, teacher,
                "B203", "周三", 3, 4, 60, "关系数据库设计、SQL 与事务管理");
        Course cs303 = upsertCourse("CS303", "操作系统", 4, 64, CourseNature.REQUIRED, teacher2,
                "C201", "周二", 1, 2, 60, "操作系统原理与实验课程");
        Course cs304 = upsertCourse("CS304", "计算机网络", 3, 48, CourseNature.REQUIRED, teacher2,
                "C302", "周四", 3, 4, 60, "网络体系结构、协议与实践");
        Course cs305 = upsertCourse("CS305", "数据结构", 4, 64, CourseNature.REQUIRED, teacher3,
                "A205", "周五", 1, 2, 55, "线性结构、树、图与算法分析");
        Course cs306 = upsertCourse("CS306", "Python程序设计", 2, 32, CourseNature.ELECTIVE, teacher3,
                "B105", "周三", 5, 6, 45, "适合跨学科学生选修的编程入门课");

        Enrollment e1 = upsertEnrollment(student1, cs301, LocalDateTime.of(2026, 3, 1, 9, 0));
        Enrollment e2 = upsertEnrollment(student1, cs302, LocalDateTime.of(2026, 3, 1, 9, 2));
        Enrollment e3 = upsertEnrollment(student1, cs303, LocalDateTime.of(2026, 3, 1, 9, 5));
        Enrollment e4 = upsertEnrollment(student6, cs301, LocalDateTime.of(2026, 3, 2, 10, 0));
        Enrollment e5 = upsertEnrollment(student6, cs304, LocalDateTime.of(2026, 3, 2, 10, 2));
        Enrollment e6 = upsertEnrollment(student6, cs306, LocalDateTime.of(2026, 3, 2, 10, 5));
        Enrollment e7 = upsertEnrollment(student7, cs302, LocalDateTime.of(2026, 3, 3, 11, 0));
        Enrollment e8 = upsertEnrollment(student7, cs303, LocalDateTime.of(2026, 3, 3, 11, 4));
        Enrollment e9 = upsertEnrollment(student7, cs305, LocalDateTime.of(2026, 3, 3, 11, 8));
        Enrollment e10 = upsertEnrollment(student2, cs301, LocalDateTime.of(2026, 3, 4, 9, 0));
        Enrollment e11 = upsertEnrollment(student2, cs305, LocalDateTime.of(2026, 3, 4, 9, 2));
        Enrollment e12 = upsertEnrollment(student2, cs306, LocalDateTime.of(2026, 3, 4, 9, 6));
        Enrollment e13 = upsertEnrollment(student3, cs302, LocalDateTime.of(2026, 3, 5, 8, 30));
        Enrollment e14 = upsertEnrollment(student3, cs304, LocalDateTime.of(2026, 3, 5, 8, 35));
        Enrollment e15 = upsertEnrollment(student3, cs305, LocalDateTime.of(2026, 3, 5, 8, 40));
        Enrollment e16 = upsertEnrollment(student4, cs301, LocalDateTime.of(2026, 3, 6, 14, 0));
        Enrollment e17 = upsertEnrollment(student4, cs304, LocalDateTime.of(2026, 3, 6, 14, 3));
        Enrollment e18 = upsertEnrollment(student4, cs306, LocalDateTime.of(2026, 3, 6, 14, 7));

        upsertGrade(e1, 88, 84, 91, 88.4, 3.0);
        upsertGrade(e2, 79, 82, 85, 82.3, 3.0);
        upsertGrade(e3, 91, 89, 93, 91.1, 4.0);
        upsertGrade(e4, 87, 80, 78, 81.1, 3.0);
        upsertGrade(e5, 92, 88, 94, 91.6, 4.0);
        upsertGrade(e6, 76, 73, 81, 77.3, 2.0);
        upsertGrade(e7, 65, 68, 72, 68.9, 1.0);
        upsertGrade(e8, 58, 60, 55, 57.4, 0.0);
        upsertGrade(e9, 82, 78, 80, 80.0, 3.0);
        upsertGrade(e10, 95, 90, 92, 92.3, 4.0);
        upsertGrade(e11, 89, 88, 90, 89.3, 4.0);
        upsertGrade(e12, 83, 80, 86, 83.9, 3.0);
        upsertGrade(e13, 78, 81, 84, 81.2, 3.0);
        upsertGrade(e14, 85, 83, 87, 85.5, 3.0);
        upsertGrade(e15, 88, 86, 89, 88.0, 4.0);
        upsertGrade(e16, 91, 88, 90, 89.9, 4.0);
        upsertGrade(e17, 86, 84, 88, 86.7, 3.0);
        upsertGrade(e18, 79, 77, 80, 79.1, 2.0);

        upsertAnnouncement("关于期中教学检查的通知",
                "请各教学班于本周五前完成教学资料自查，管理员将在下周统一抽检。",
                true, LocalDateTime.of(2026, 4, 1, 9, 0), admin);
        upsertAnnouncement("2026年春季学期选修课补选安排",
                "补选通道将于本周三 12:00 开放，请同学们在规定时间内完成课程调整。",
                true, LocalDateTime.of(2026, 4, 2, 10, 30), teacher);
        upsertAnnouncement("实验室开放时间调整",
                "A 楼机房周末开放时间调整为 09:00-18:00，请合理安排实验与课程设计。",
                false, LocalDateTime.of(2026, 4, 3, 14, 0), teacher2);
        upsertAnnouncement("毕业设计中期检查说明",
                "毕业班学生须在系统中提交中期进展材料，指导老师完成线上审核。",
                false, LocalDateTime.of(2026, 4, 4, 16, 20), admin);

        upsertLeave(student1, "发热就诊，需前往医院复查", LocalDate.of(2026, 4, 8), LocalDate.of(2026, 4, 9),
                LeaveStatus.APPROVED, "已核验请假材料，请按时返校。", true, LocalDateTime.of(2026, 4, 6, 8, 15), teacher);
        upsertLeave(student2, "参加省级程序设计竞赛", LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 12),
                LeaveStatus.APPROVED, "同意外出参赛，返校后补交证明。", true, LocalDateTime.of(2026, 4, 6, 11, 0), teacher2);
        upsertLeave(student4, "家庭事务需返乡处理", LocalDate.of(2026, 4, 11), LocalDate.of(2026, 4, 13),
                LeaveStatus.PENDING, "", false, LocalDateTime.of(2026, 4, 6, 20, 10), null);

        upsertStatusChange(student1, StudentStatusType.ENROLL, "未入学", "在读", "完成新生报到注册",
                LocalDate.of(2026, 9, 1));
        upsertStatusChange(student2, StudentStatusType.ENROLL, "未入学", "在读", "完成学籍注册",
                LocalDate.of(2026, 9, 1));
        upsertStatusChange(student3, StudentStatusType.CHANGE_MAJOR, "软件工程", "计算机科学与技术", "根据培养方案调整专业方向",
                LocalDate.of(2026, 2, 18));
        upsertStatusChange(student4, StudentStatusType.RESUME, "休学", "在读", "已完成复学审批",
                LocalDate.of(2026, 3, 5));
        upsertStatusChange(student7, StudentStatusType.SUSPEND, "在读", "休学", "因个人健康原因暂缓学习",
                LocalDate.of(2026, 1, 12));
    }

    private AppUser upsertUser(String username, String rawPassword, String fullName, UserRole role, String phone, String avatarUrl) {
        AppUser user = appUserRepository.findByUsername(username).orElseGet(AppUser::new);
        user.setUsername(username);
        user.setFullName(fullName);
        user.setRole(role);
        user.setPhone(phone);
        user.setAvatarUrl(avatarUrl);
        user.setEnabled(Boolean.TRUE);
        if (user.getPassword() == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }
        return appUserRepository.save(user);
    }

    private Student upsertStudent(AppUser user, String studentNo, String name, int age, String gender, String classroom,
                                  String phone, String major, LocalDate enrollmentDate, String currentStatus, String photoUrl) {
        Student student = studentRepository.findByUserId(user.getId()).orElseGet(Student::new);
        student.setUser(user);
        student.setStudentNo(studentNo);
        student.setName(name);
        student.setAge(age);
        student.setGender(gender);
        student.setClassroom(classroom);
        student.setPhone(phone);
        student.setMajor(major);
        student.setEnrollmentDate(enrollmentDate);
        student.setCurrentStatus(currentStatus);
        student.setPhotoUrl(photoUrl);
        return studentRepository.save(student);
    }

    private Student upsertStandaloneStudent(String studentNo, String name, int age, String gender, String classroom,
                                            String phone, String major, LocalDate enrollmentDate, String currentStatus, String photoUrl) {
        Student student = studentRepository.findAll().stream()
                .filter(item -> studentNo.equals(item.getStudentNo()))
                .findFirst()
                .orElseGet(Student::new);
        student.setStudentNo(studentNo);
        student.setName(name);
        student.setAge(age);
        student.setGender(gender);
        student.setClassroom(classroom);
        student.setPhone(phone);
        student.setMajor(major);
        student.setEnrollmentDate(enrollmentDate);
        student.setCurrentStatus(currentStatus);
        student.setPhotoUrl(photoUrl);
        return studentRepository.save(student);
    }

    private Course upsertCourse(String courseCode, String courseName, int credit, int classHours, CourseNature nature,
                                AppUser teacher, String classroom, String weekDay, int startSection, int endSection,
                                int capacity, String description) {
        Course course = courseRepository.findAll().stream()
                .filter(item -> courseCode.equals(item.getCourseCode()))
                .findFirst()
                .orElseGet(Course::new);
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setCredit(credit);
        course.setClassHours(classHours);
        course.setNature(nature);
        course.setTeacher(teacher);
        course.setClassroom(classroom);
        course.setWeekDay(weekDay);
        course.setStartSection(startSection);
        course.setEndSection(endSection);
        course.setCapacity(capacity);
        course.setDescription(description);
        return courseRepository.save(course);
    }

    private Enrollment upsertEnrollment(Student student, Course course, LocalDateTime selectedAt) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId())
                .orElseGet(Enrollment::new);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setSelectedAt(selectedAt);
        return enrollmentRepository.save(enrollment);
    }

    private void upsertGrade(Enrollment enrollment, double dailyScore, double midtermScore, double finalScore,
                             double totalScore, double gpa) {
        GradeRecord gradeRecord = gradeRecordRepository.findByEnrollmentId(enrollment.getId())
                .orElseGet(GradeRecord::new);
        gradeRecord.setEnrollment(enrollment);
        gradeRecord.setDailyScore(dailyScore);
        gradeRecord.setMidtermScore(midtermScore);
        gradeRecord.setFinalScore(finalScore);
        gradeRecord.setTotalScore(totalScore);
        gradeRecord.setGpa(gpa);
        gradeRecordRepository.save(gradeRecord);
    }

    private void upsertAnnouncement(String title, String content, boolean pinned, LocalDateTime publishedAt, AppUser publisher) {
        Announcement announcement = announcementRepository.findAll().stream()
                .filter(item -> title.equals(item.getTitle()))
                .findFirst()
                .orElseGet(Announcement::new);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setPinned(pinned);
        announcement.setPublishedAt(publishedAt);
        announcement.setPublisher(publisher);
        announcementRepository.save(announcement);
    }

    private void upsertLeave(Student student, String reason, LocalDate startDate, LocalDate endDate, LeaveStatus status,
                             String approvalComment, boolean attendanceLinked, LocalDateTime submittedAt, AppUser approver) {
        LeaveRequest leaveRequest = leaveRequestRepository.findAll().stream()
                .filter(item -> item.getStudent() != null
                        && item.getStudent().getId().equals(student.getId())
                        && reason.equals(item.getReason()))
                .findFirst()
                .orElseGet(LeaveRequest::new);
        leaveRequest.setStudent(student);
        leaveRequest.setReason(reason);
        leaveRequest.setStartDate(startDate);
        leaveRequest.setEndDate(endDate);
        leaveRequest.setStatus(status);
        leaveRequest.setApprovalComment(approvalComment);
        leaveRequest.setAttendanceLinked(attendanceLinked);
        leaveRequest.setSubmittedAt(submittedAt);
        leaveRequest.setApprover(approver);
        leaveRequestRepository.save(leaveRequest);
    }

    private void upsertStatusChange(Student student, StudentStatusType changeType, String beforeStatus, String afterStatus,
                                    String reason, LocalDate changeDate) {
        List<StudentStatusChange> existingChanges = studentStatusChangeRepository.findByStudentIdOrderByChangeDateDesc(student.getId());
        StudentStatusChange statusChange = existingChanges.stream()
                .filter(item -> item.getChangeType() == changeType && changeDate.equals(item.getChangeDate()))
                .findFirst()
                .orElseGet(StudentStatusChange::new);
        statusChange.setStudent(student);
        statusChange.setChangeType(changeType);
        statusChange.setBeforeStatus(beforeStatus);
        statusChange.setAfterStatus(afterStatus);
        statusChange.setReason(reason);
        statusChange.setChangeDate(changeDate);
        studentStatusChangeRepository.save(statusChange);
    }
}
