package com.williams.workingdayservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Object representation to support configuration of all business working days.
 *
 * @author williams.adeho
 */
@Component
@ConfigurationProperties(prefix = "configurable.days")
public class WorkingDayServiceProperties {

    private List<String> workingDays;

    public List<String> getWorkingDays() {
        return workingDays;
    }

    // need setter for configuration properties to work
    public void setWorkingDays(List<String> workingDays) {
        this.workingDays = workingDays;
    }
}
