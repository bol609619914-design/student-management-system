package com.example.studentms.service;

import com.example.studentms.model.Announcement;
import com.example.studentms.model.Course;
import com.example.studentms.model.Enrollment;
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
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("周一", 0);
        map.put("周二", 0);
        map.put("周三", 0);
        map.put("周四", 0);
        map.put("周五", 0);
        for (Course course : courseService.findAllCourses()) {
            map.computeIfPresent(course.getWeekDay(), (key, value) -> value + 1);
        }
        return map;
    }

    public int maxWeekdayCourseLoad() {
        return buildWeekdayCourseLoad().values().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(1);
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

    private double averageGpa() {
        return Math.round(ranking().stream()
                .mapToDouble(item -> (Double) item.get("gpa"))
                .average()
                .orElse(0) * 100.0) / 100.0;
    }
}
