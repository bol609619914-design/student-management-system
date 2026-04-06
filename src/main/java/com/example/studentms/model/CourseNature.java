package com.example.studentms.model;

public enum CourseNature {
    REQUIRED("必修"),
    ELECTIVE("选修");

    private final String label;

    CourseNature(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
