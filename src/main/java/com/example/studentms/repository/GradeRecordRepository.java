package com.example.studentms.repository;

import com.example.studentms.model.GradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRecordRepository extends JpaRepository<GradeRecord, Long> {

    Optional<GradeRecord> findByEnrollmentId(Long enrollmentId);

    List<GradeRecord> findByEnrollmentStudentId(Long studentId);
}
