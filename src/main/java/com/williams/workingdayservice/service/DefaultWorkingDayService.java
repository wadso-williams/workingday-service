package com.williams.workingdayservice.service;

import com.williams.workingdayservice.dto.WorkingDay;
import com.williams.workingdayservice.WorkingDayServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Default implementation of the working day service, to enable a user get the next working day from a given date.
 *
 * @author williams.adeho
 */
@Service
public class DefaultWorkingDayService implements WorkingDayService {

    private WorkingDayServiceProperties serviceProperties;

    /**
     * Creates a default working day service with the specified service properties
     *
     * @param serviceProperties an object representation of the service properties
     */
    @Autowired
    public DefaultWorkingDayService(WorkingDayServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    /**
     * Find the next working day based on the given date.
     *
     * @param date the date from which to find the next working day
     * @return the next working day
     */
    public WorkingDay getNextWorkingDay(LocalDate date) {
        List<DayOfWeek> workingDayList = serviceProperties.getWorkingDays().stream().map(DayOfWeek::valueOf).collect(Collectors.toList());
        if(Objects.isNull(date)) {
            date = LocalDate.now();
        }
        return nextWorkingDay(date.plusDays(1), workingDayList);
    }

    private WorkingDay nextWorkingDay(LocalDate fromDate, List<DayOfWeek> workingDayList) {
        if (workingDayList.contains(fromDate.getDayOfWeek())) {
            return new WorkingDay(fromDate.getDayOfWeek().toString(), fromDate);
        } else {
            return nextWorkingDay(fromDate.plusDays(1), workingDayList);
        }
    }
}