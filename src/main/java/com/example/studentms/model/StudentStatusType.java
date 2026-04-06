package com.example.studentms.model;

public enum StudentStatusType {
    ENROLL("入学"),
    CHANGE_MAJOR("转专业"),
    REPEAT("留级"),
    SUSPEND("休学"),
    RESUME("复学");

    private final String label;

    StudentStatusType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
