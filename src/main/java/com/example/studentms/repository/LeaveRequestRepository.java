package com.example.studentms.repository;

import com.example.studentms.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findAllByOrderBySubmittedAtDesc();

    List<LeaveRequest> findByStudentIdOrderBySubmittedAtDesc(Long studentId);
}
