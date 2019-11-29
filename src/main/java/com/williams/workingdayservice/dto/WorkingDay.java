package com.williams.workingdayservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Data transfer Object to represent a working day.
 *
 * @author williams.adeho
 */
public class WorkingDay {

    @JsonProperty("day_of_week")
    private String dayOfWeek;

    @JsonProperty("date")
    private LocalDate date;

    public WorkingDay() {
    }

    /**
     * Creates a working day with the specified day of week and date
     *
     * @param dayOfWeek the working day of the week
     * @param date the working date
     */
    public WorkingDay(String dayOfWeek, LocalDate date) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
    }

    /**
     * Return the day of the week of the working date
     *
     * @return day of the week of the working date
     */
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Sets the day of the week of the working date
     *
     * @param dayOfWeek day of the week of the working date
     */
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Return the working date
     *
     * @return the working date
     * @see  LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the working date
     *
     * @param date the working date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

}