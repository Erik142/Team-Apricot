package com.teamapricot.projectwalking.model;

public class Statistics {
    private String title;
    private String value;

    public Statistics(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return this.title;
    }

    public String getValue() {
        return this.value;
    }
}
