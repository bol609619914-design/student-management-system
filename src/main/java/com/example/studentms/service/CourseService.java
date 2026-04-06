package com.example.studentms.service;

import com.example.studentms.model.Course;
import com.example.studentms.model.CourseNature;
import com.example.studentms.model.Enrollment;
import com.example.studentms.model.Student;
import com.example.studentms.model.UserRole;
import com.example.studentms.repository.AppUserRepository;
import com.example.studentms.repository.CourseRepository;
import com.example.studentms.repository.EnrollmentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AppUserRepository appUserRepository;

    public CourseService(CourseRepository courseRepository,
                         EnrollmentRepository enrollmentRepository,
                         AppUserRepository appUserRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.appUserRepository = appUserRepository;
    }

    @PostConstruct
    public void initCourses() {
        if (courseRepository.count() > 0) {
            return;
        }
        appUserRepository.findByRole(UserRole.TEACHER).stream().findFirst().ifPresent(teacher -> {
            Course javaWeb = new Course();
            javaWeb.setCourseCode("CS301");
            javaWeb.setCourseName("Java Web 开发");
            javaWeb.setCredit(3);
            javaWeb.setClassHours(48);
            javaWeb.setNature(CourseNature.REQUIRED);
            javaWeb.setTeacher(teacher);
            javaWeb.setClassroom("A301");
            javaWeb.setWeekDay("周一");
            javaWeb.setStartSection(1);
            javaWeb.setEndSection(2);
            javaWeb.setCapacity(60);
            javaWeb.setDescription("面向企业级开发的 Java Web 核心课程");
            courseRepository.save(javaWeb);

            Course database = new Course();
            database.setCourseCode("CS302");
            database.setCourseName("数据库系统");
            database.setCredit(4);
            database.setClassHours(64);
            database.setNature(CourseNature.REQUIRED);
            database.setTeacher(teacher);
            database.setClassroom("B203");
            database.setWeekDay("周三");
            database.setStartSection(3);
            database.setEndSection(4);
            database.setCapacity(50);
            database.setDescription("覆盖 SQL、事务、索引与数据库设计");
            courseRepository.save(database);
        });
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAllByOrderByCourseCodeAsc();
    }

    public List<Enrollment> findAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Course findCourse(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public void saveCourse(Course course) {
        validateCourseConflict(course);
        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    private void validateCourseConflict(Course current) {
        for (Course existing : courseRepository.findAll()) {
            if (current.getId() != null && current.getId().equals(existing.getId())) {
                continue;
            }
            if (!sameDay(current, existing)) {
                continue;
            }
            if (!overlap(current.getStartSection(), current.getEndSection(), existing.getStartSection(), existing.getEndSection())) {
                continue;
            }
            if (current.getClassroom() != null && current.getClassroom().equals(existing.getClassroom())) {
                throw new IllegalArgumentException("排课冲突：同一教室同一时段不能安排两门课");
            }
            if (current.getTeacher() != null && existing.getTeacher() != null
                    && current.getTeacher().getId().equals(existing.getTeacher().getId())) {
                throw new IllegalArgumentException("排课冲突：同一教师同一时段不能教授两门课");
            }
        }
    }

    public void enroll(Student student, Long courseId) {
        Course course = findCourse(courseId);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalArgumentException("你已经选过这门课程");
        }
        if (course.getCapacity() != null && enrollmentRepository.countByCourseId(courseId) >= course.getCapacity()) {
            throw new IllegalArgumentException("该课程人数已满");
        }

        List<Enrollment> currentEnrollments = enrollmentRepository.findByStudentId(student.getId());
        for (Enrollment enrollment : currentEnrollments) {
            Course existing = enrollment.getCourse();
            if (sameDay(course, existing)
                    && overlap(course.getStartSection(), course.getEndSection(), existing.getStartSection(), existing.getEndSection())) {
                throw new IllegalArgumentException("选课冲突：当前课程与已选课程时间重叠");
            }
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setSelectedAt(LocalDateTime.now());
        enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> findEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Course> findCoursesForTeacher(Long teacherId) {
        List<Course> result = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            if (course.getTeacher() != null && teacherId.equals(course.getTeacher().getId())) {
                result.add(course);
            }
        }
        return result;
    }

    private boolean sameDay(Course left, Course right) {
        return left.getWeekDay() != null && left.getWeekDay().equals(right.getWeekDay());
    }

    private boolean overlap(Integer start1, Integer end1, Integer start2, Integer end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        return start1 <= end2 && start2 <= end1;
    }
}
