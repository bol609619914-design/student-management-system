package com.example.studentms.model;

public enum LeaveStatus {
    PENDING("待审批"),
    APPROVED("已同意"),
    REJECTED("已驳回");

    private final String label;

    LeaveStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
