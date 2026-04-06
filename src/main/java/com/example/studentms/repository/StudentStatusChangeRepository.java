package com.example.studentms.repository;

import com.example.studentms.model.StudentStatusChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentStatusChangeRepository extends JpaRepository<StudentStatusChange, Long> {

    List<StudentStatusChange> findByStudentIdOrderByChangeDateDesc(Long studentId);
}
