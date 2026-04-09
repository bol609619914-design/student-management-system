package com.example.studentms.repository;

import com.example.studentms.model.LeaveRequest;
import com.example.studentms.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findAllByOrderBySubmittedAtDesc();

    List<LeaveRequest> findByStudentIdOrderBySubmittedAtDesc(Long studentId);

    List<LeaveRequest> findByStatusOrderBySubmittedAtDesc(LeaveStatus status);

    List<LeaveRequest> findByStatusNotOrderBySubmittedAtDesc(LeaveStatus status);

    void deleteByStudentId(Long studentId);
}
