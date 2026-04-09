package com.example.studentms.service;

import com.example.studentms.model.AppUser;
import com.example.studentms.model.Student;
import com.example.studentms.model.StudentStatusChange;
import com.example.studentms.model.StudentStatusType;
import com.example.studentms.model.UserRole;
import com.example.studentms.repository.AppUserRepository;
import com.example.studentms.repository.EnrollmentRepository;
import com.example.studentms.repository.GradeRecordRepository;
import com.example.studentms.repository.LeaveRequestRepository;
import com.example.studentms.repository.StudentRepository;
import com.example.studentms.repository.StudentStatusChangeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final StudentStatusChangeRepository statusChangeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRecordRepository gradeRecordRepository;

    public StudentService(AppUserRepository appUserRepository,
                          StudentRepository studentRepository,
                          StudentStatusChangeRepository statusChangeRepository,
                          LeaveRequestRepository leaveRequestRepository,
                          EnrollmentRepository enrollmentRepository,
                          GradeRecordRepository gradeRecordRepository) {
        this.appUserRepository = appUserRepository;
        this.studentRepository = studentRepository;
        this.statusChangeRepository = statusChangeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.gradeRecordRepository = gradeRecordRepository;
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

    public Student findOrCreateByUserId(Long userId) {
        Student existingStudent = findByUserId(userId);
        if (existingStudent != null) {
            return existingStudent;
        }

        AppUser user = appUserRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != UserRole.STUDENT) {
            return null;
        }

        Student student = new Student();
        student.setUser(user);
        student.setStudentNo("S" + user.getId() + System.currentTimeMillis() % 10000);
        student.setName(user.getFullName() == null || user.getFullName().isBlank() ? user.getUsername() : user.getFullName());
        student.setAge(18);
        student.setGender("男");
        student.setClassroom("待分班");
        student.setPhone(user.getPhone() == null || user.getPhone().isBlank() ? "未填写" : user.getPhone());
        student.setMajor("待分配专业");
        student.setEnrollmentDate(LocalDate.now());
        student.setCurrentStatus("在读");
        student.setPhotoUrl(user.getAvatarUrl());
        return studentRepository.save(student);
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

    @Transactional
    public void deleteById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("学生信息不存在"));

        gradeRecordRepository.deleteByEnrollmentStudentId(id);
        enrollmentRepository.deleteByStudentId(id);
        leaveRequestRepository.deleteByStudentId(id);
        statusChangeRepository.deleteByStudentId(id);

        AppUser linkedUser = student.getUser();
        studentRepository.delete(student);

        if (linkedUser != null && linkedUser.getRole() == UserRole.STUDENT) {
            linkedUser.setEnabled(Boolean.FALSE);
            appUserRepository.save(linkedUser);
        }
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
