package com.example.studentms.service;

import com.example.studentms.model.Enrollment;
import com.example.studentms.model.GradeRecord;
import com.example.studentms.model.Student;
import com.example.studentms.repository.EnrollmentRepository;
import com.example.studentms.repository.GradeRecordRepository;
import com.example.studentms.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GradeService {

    private final GradeRecordRepository gradeRecordRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    public GradeService(GradeRecordRepository gradeRecordRepository,
                        EnrollmentRepository enrollmentRepository,
                        StudentRepository studentRepository) {
        this.gradeRecordRepository = gradeRecordRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }

    public List<GradeRecord> findAllGrades() {
        return gradeRecordRepository.findAll();
    }

    public List<GradeRecord> findGradesByStudent(Long studentId) {
        return gradeRecordRepository.findByEnrollmentStudentId(studentId);
    }

    public void saveGrade(Long enrollmentId, Double daily, Double midterm, Double finals) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("选课记录不存在"));
        GradeRecord record = gradeRecordRepository.findByEnrollmentId(enrollmentId).orElseGet(GradeRecord::new);
        record.setEnrollment(enrollment);
        record.setDailyScore(daily);
        record.setMidtermScore(midterm);
        record.setFinalScore(finals);
        double total = daily * 0.3 + midterm * 0.3 + finals * 0.4;
        record.setTotalScore(round(total));
        record.setGpa(calculateGpa(total));
        gradeRecordRepository.save(record);
    }

    public double calculateStudentAverageGpa(Long studentId) {
        return round(findGradesByStudent(studentId).stream().mapToDouble(GradeRecord::getGpa).average().orElse(0));
    }

    public List<Map<String, Object>> buildRanking() {
        List<Map<String, Object>> ranking = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            Map<String, Object> item = new HashMap<>();
            item.put("student", student);
            item.put("gpa", calculateStudentAverageGpa(student.getId()));
            ranking.add(item);
        }
        ranking.sort(Comparator.comparing((Map<String, Object> map) -> (Double) map.get("gpa")).reversed());
        int index = 1;
        for (Map<String, Object> item : ranking) {
            item.put("rank", index++);
        }
        return ranking;
    }

    public long countFailedCourses() {
        return gradeRecordRepository.findAll().stream().filter(item -> item.getTotalScore() != null && item.getTotalScore() < 60).count();
    }

    public double passRate() {
        List<GradeRecord> records = gradeRecordRepository.findAll();
        if (records.isEmpty()) {
            return 0;
        }
        long passed = records.stream().filter(item -> item.getTotalScore() != null && item.getTotalScore() >= 60).count();
        return round(passed * 100.0 / records.size());
    }

    private double calculateGpa(double total) {
        if (total >= 90) {
            return 4.0;
        }
        if (total >= 80) {
            return 3.0;
        }
        if (total >= 70) {
            return 2.0;
        }
        if (total >= 60) {
            return 1.0;
        }
        return 0.0;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
