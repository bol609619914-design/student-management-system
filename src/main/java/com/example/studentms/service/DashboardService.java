package com.example.studentms.service;

import com.example.studentms.model.Announcement;
import com.example.studentms.model.AppUser;
import com.example.studentms.model.Course;
import com.example.studentms.model.Enrollment;
import com.example.studentms.model.GradeRecord;
import com.example.studentms.model.LeaveRequest;
import com.example.studentms.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final StudentService studentService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final LeaveService leaveService;
    private final AnnouncementService announcementService;

    public DashboardService(StudentService studentService,
                            CourseService courseService,
                            GradeService gradeService,
                            LeaveService leaveService,
                            AnnouncementService announcementService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.leaveService = leaveService;
        this.announcementService = announcementService;
    }

    public Map<String, Object> buildStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("studentCount", studentService.count());
        stats.put("courseCount", courseService.findAllCourses().size());
        stats.put("passRate", gradeService.passRate());
        stats.put("leaveCount", leaveService.findAll().size());
        stats.put("maleCount", studentService.countMale());
        stats.put("femaleCount", studentService.countFemale());
        stats.put("failedCount", gradeService.countFailedCourses());
        stats.put("avgGpa", averageGpa());
        stats.put("enrollmentCount", courseService.findAllEnrollments().size());
        return stats;
    }

    public Map<String, Object> buildStudentDashboard(AppUser currentUser, Student student) {
        Map<String, Object> dashboard = new HashMap<>();
        List<Enrollment> myEnrollments = student == null ? List.of() : courseService.findEnrollmentsByStudent(student.getId());
        List<GradeRecord> myGrades = student == null ? List.of() : gradeService.findGradesByStudent(student.getId());
        List<LeaveRequest> myLeaves = student == null ? List.of() : leaveService.findStudentLeaves(student.getId());

        dashboard.put("selectedCourseCount", myEnrollments.size());
        dashboard.put("avgGpa", student == null ? 0.0 : gradeService.calculateStudentAverageGpa(student.getId()));
        dashboard.put("leaveCount", myLeaves.size());
        dashboard.put("recentAnnouncements", recentAnnouncements());
        dashboard.put("myCourses", myEnrollments.stream().limit(5).toList());
        dashboard.put("myGrades", myGrades.stream().limit(5).toList());
        dashboard.put("myLeaves", myLeaves.stream().limit(4).toList());
        dashboard.put("currentStatus", student == null ? "未绑定学籍" : safeText(student.getCurrentStatus(), "未绑定学籍"));
        dashboard.put("major", student == null ? "未填写" : safeText(student.getMajor(), "未填写"));
        dashboard.put("classroom", student == null ? "未分班" : safeText(student.getClassroom(), "未分班"));
        dashboard.put("welcomeName", currentUser.getFullName());
        return dashboard;
    }

    public Map<String, Object> buildTeacherDashboard(AppUser currentUser) {
        Map<String, Object> dashboard = new HashMap<>();
        List<Course> myCourses = courseService.findCoursesForTeacher(currentUser.getId());
        List<Enrollment> allEnrollments = courseService.findAllEnrollments();
        List<GradeRecord> allGrades = gradeService.findAllGrades();
        List<Long> myCourseIds = myCourses.stream().map(Course::getId).toList();
        long studentCount = allEnrollments.stream()
                .filter(item -> myCourseIds.contains(item.getCourse().getId()))
                .map(item -> item.getStudent().getId())
                .distinct()
                .count();
        long gradedCount = allGrades.stream()
                .filter(item -> myCourseIds.contains(item.getEnrollment().getCourse().getId()))
                .count();
        Map<String, Integer> teachingLoad = buildTeacherWeekLoad(myCourses);

        dashboard.put("welcomeName", currentUser.getFullName());
        dashboard.put("courseCount", myCourses.size());
        dashboard.put("studentCount", studentCount);
        dashboard.put("gradedCount", gradedCount);
        dashboard.put("myCourses", myCourses.stream().limit(5).toList());
        dashboard.put("recentAnnouncements", recentAnnouncements());
        dashboard.put("recentLeaves", recentLeaves());
        dashboard.put("teachingLoad", teachingLoad);
        dashboard.put("maxTeachingLoad", maxValue(teachingLoad));
        return dashboard;
    }

    public List<Map<String, Object>> ranking() {
        return gradeService.buildRanking();
    }

    public Map<String, Integer> buildScheduleHeatMap() {
        Map<String, Integer> map = new HashMap<>();
        for (Student student : studentService.findAll()) {
            List<Enrollment> enrollments = courseService.findEnrollmentsByStudent(student.getId());
            map.put(student.getName(), enrollments.size());
        }
        return map;
    }

    public Map<String, Integer> buildWeekdayCourseLoad() {
        Map<String, Integer> map = defaultWeekMap();
        for (Course course : courseService.findAllCourses()) {
            map.computeIfPresent(course.getWeekDay(), (key, value) -> value + 1);
        }
        return map;
    }

    public int maxWeekdayCourseLoad() {
        return maxValue(buildWeekdayCourseLoad());
    }

    public List<Map<String, Object>> topCourses() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Course course : courseService.findAllCourses()) {
            long selected = courseService.findAllEnrollments().stream()
                    .filter(item -> item.getCourse().getId().equals(course.getId()))
                    .count();
            Map<String, Object> item = new HashMap<>();
            item.put("course", course);
            item.put("selected", selected);
            item.put("usageRate", course.getCapacity() == null || course.getCapacity() == 0
                    ? 0L
                    : Math.round(selected * 100.0 / course.getCapacity()));
            result.add(item);
        }
        result.sort(Comparator.comparing((Map<String, Object> item) -> ((Long) item.get("selected"))).reversed());
        return result.stream().limit(5).toList();
    }

    public List<Announcement> recentAnnouncements() {
        return announcementService.findAll().stream().limit(4).toList();
    }

    public List<LeaveRequest> recentLeaves() {
        return leaveService.findAll().stream().limit(5).toList();
    }

    private Map<String, Integer> buildTeacherWeekLoad(List<Course> courses) {
        Map<String, Integer> map = defaultWeekMap();
        for (Course course : courses) {
            map.computeIfPresent(course.getWeekDay(), (key, value) -> value + 1);
        }
        return map;
    }

    private Map<String, Integer> defaultWeekMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("周一", 0);
        map.put("周二", 0);
        map.put("周三", 0);
        map.put("周四", 0);
        map.put("周五", 0);
        return map;
    }

    private int maxValue(Map<String, Integer> map) {
        return map.values().stream().mapToInt(Integer::intValue).max().orElse(1);
    }

    private String safeText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private double averageGpa() {
        return Math.round(ranking().stream()
                .mapToDouble(item -> (Double) item.get("gpa"))
                .average()
                .orElse(0) * 100.0) / 100.0;
    }
}
