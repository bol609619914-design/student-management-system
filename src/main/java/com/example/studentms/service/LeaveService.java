package com.example.studentms.service;

import com.example.studentms.model.AppUser;
import com.example.studentms.model.LeaveRequest;
import com.example.studentms.model.LeaveStatus;
import com.example.studentms.model.Student;
import com.example.studentms.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public List<LeaveRequest> findAll() {
        return leaveRequestRepository.findAllByOrderBySubmittedAtDesc();
    }

    public List<LeaveRequest> findStudentLeaves(Long studentId) {
        return leaveRequestRepository.findByStudentIdOrderBySubmittedAtDesc(studentId);
    }

    public void submit(Student student, String reason, LocalDate startDate, LocalDate endDate) {
        LeaveRequest request = new LeaveRequest();
        request.setStudent(student);
        request.setReason(reason);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStatus(LeaveStatus.PENDING);
        request.setSubmittedAt(LocalDateTime.now());
        request.setAttendanceLinked(Boolean.FALSE);
        leaveRequestRepository.save(request);
    }

    public void approve(Long leaveId, AppUser approver, boolean approved, String comment) {
        LeaveRequest request = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("请假记录不存在"));
        request.setApprover(approver);
        request.setApprovalComment(comment);
        request.setStatus(approved ? LeaveStatus.APPROVED : LeaveStatus.REJECTED);
        request.setAttendanceLinked(approved);
        leaveRequestRepository.save(request);
    }
}
