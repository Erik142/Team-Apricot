package com.teamapricot.projectwalking.model;

/**
 * @author Erik Wahlberger
 * @version 2021-10-26
 *
 * A data class to hold simple statistics data
 */
public class Statistics {
    private String title;
    private String value;

    /**
     * Creates a new instance of the Statistics class
     * @param title The title of the statistic
     * @param value The value of the statistic
     */
    public Statistics(String title, String value) {
        this.title = title;
        this.value = value;
    }

    /**
     * Retrieves the statistic title
     * @return The title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Retrieves the statistic value
     * @return The value
     */
    public String getValue() {
        return this.value;
    }
}
