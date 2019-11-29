package com.williams.workingdayservice.service;

import com.williams.workingdayservice.dto.WorkingDay;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

/**
 * Interface to enable a user get the next working day from a given date.
 *
 * @author williams.adeho
 */
@Validated
public interface WorkingDayService {

    /**
     * Find the next working day based on the given date.
     *
     * @param date the date from which to find the next working day
     * @return the next working day
     */
    WorkingDay getNextWorkingDay(LocalDate date);
}
