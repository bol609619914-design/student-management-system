package com.example.studentms.service;

import com.example.studentms.model.Student;
import com.example.studentms.model.StudentStatusChange;
import com.example.studentms.model.StudentStatusType;
import com.example.studentms.repository.StudentRepository;
import com.example.studentms.repository.StudentStatusChangeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentStatusChangeRepository statusChangeRepository;

    public StudentService(StudentRepository studentRepository, StudentStatusChangeRepository statusChangeRepository) {
        this.studentRepository = studentRepository;
        this.statusChangeRepository = statusChangeRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student findByUserId(Long userId) {
        return studentRepository.findByUserId(userId).orElse(null);
    }

    public void save(Student student) {
        if (student.getStudentNo() == null || student.getStudentNo().isBlank()) {
            student.setStudentNo("2026" + (System.currentTimeMillis() % 100000));
        }
        if (student.getEnrollmentDate() == null) {
            student.setEnrollmentDate(LocalDate.now());
        }
        if (student.getCurrentStatus() == null || student.getCurrentStatus().isBlank()) {
            student.setCurrentStatus("在读");
        }
        studentRepository.save(student);
    }

    public void update(Student student) {
        if (student.getId() == null || !studentRepository.existsById(student.getId())) {
            throw new IllegalArgumentException("学生信息不存在");
        }
        studentRepository.save(student);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public long count() {
        return studentRepository.count();
    }

    public double averageAge() {
        List<Student> students = studentRepository.findAll();
        return students.stream().mapToInt(Student::getAge).average().orElse(0);
    }

    public long countMale() {
        return studentRepository.countByGender("男");
    }

    public long countFemale() {
        return studentRepository.countByGender("女");
    }

    public List<StudentStatusChange> getStatusHistory(Long studentId) {
        return statusChangeRepository.findByStudentIdOrderByChangeDateDesc(studentId);
    }

    public void addStatusChange(Long studentId, StudentStatusType type, String afterStatus, String reason) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("学生不存在");
        }
        StudentStatusChange change = new StudentStatusChange();
        change.setStudent(student);
        change.setChangeType(type);
        change.setBeforeStatus(student.getCurrentStatus());
        change.setAfterStatus(afterStatus);
        change.setReason(reason);
        change.setChangeDate(LocalDate.now());
        statusChangeRepository.save(change);

        if (type == StudentStatusType.CHANGE_MAJOR && afterStatus != null && !afterStatus.isBlank()) {
            student.setMajor(afterStatus);
        } else {
            student.setCurrentStatus(afterStatus);
        }
        studentRepository.save(student);
    }
}
