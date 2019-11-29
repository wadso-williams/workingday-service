package com.williams.workingdayservice.controller;

import com.williams.workingdayservice.dto.WorkingDay;
import com.williams.workingdayservice.service.WorkingDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * REST controller for interacting with Next Working Day service.
 *
 * @author williams.adeho
 */
@RestController("/next-working-day")
public class NextWorkingDayServiceController {

    private final WorkingDayService workingDayService;

    /**
     * Creates a working day service controller by injecting a working day service.
     *
     * @param workingDayService the injected working day service
     */
    @Autowired
    public NextWorkingDayServiceController(WorkingDayService workingDayService) {
        this.workingDayService = workingDayService;
    }

    /**
     * Find the next working day based on the given date.
     *
     * @param date the date from which to find the next working day
     * @return the next working day
     */
    @GetMapping
    public WorkingDay getNextWorkingDay(@RequestParam(value = "after", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return workingDayService.getNextWorkingDay(date);
    }
}
