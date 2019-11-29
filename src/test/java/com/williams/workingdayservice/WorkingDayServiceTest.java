package com.williams.workingdayservice;

import com.williams.workingdayservice.dto.WorkingDay;
import com.williams.workingdayservice.service.DefaultWorkingDayService;
import com.williams.workingdayservice.service.WorkingDayService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;

import static org.junit.Assert.assertThat;

/**
 * Test cases for {@link com.williams.workingdayservice.service.WorkingDayService}
 */
public class WorkingDayServiceTest {

    private WorkingDayService workingDayService;

    private WorkingDayServiceProperties workingDayServiceProperties;

    @Before
    public void setup() {
        workingDayServiceProperties = new WorkingDayServiceProperties();
        workingDayServiceProperties.setWorkingDays(Arrays.asList("MONDAY",
                "TUESDAY",
                "WEDNESDAY",
                "THURSDAY",
                "FRIDAY"));
        workingDayService = new DefaultWorkingDayService(workingDayServiceProperties);
    }

    @Test
    public void shouldGetNextWorkingDay() {
        //Given
        final LocalDate fromDate = LocalDate.parse("2019-04-04");

        // when
        final WorkingDay workingDay = workingDayService.getNextWorkingDay(fromDate);

        // Then
        assertThat(workingDay.getDate(), Matchers.equalTo(LocalDate.parse("2019-04-05")));
        assertThat(workingDay.getDayOfWeek(), Matchers.equalTo("FRIDAY"));
    }

    @Test
    public void shouldGetNextWorkingDayAfterNonWorkingDay() {
        // Given
        final LocalDate fromDate = LocalDate.parse("2019-04-05");

        // when
        final WorkingDay workingDay = workingDayService.getNextWorkingDay(fromDate);

        // Then
        assertThat(workingDay.getDate(), Matchers.equalTo(LocalDate.parse("2019-04-08")));
        assertThat(workingDay.getDayOfWeek(), Matchers.equalTo("MONDAY"));
    }

    @Test
    public void shouldGetNextWorkingDayAsFridayIfThursdayIsNonWorkingDay() {
        //Given
        workingDayServiceProperties.setWorkingDays(Arrays.asList("MONDAY",
                "TUESDAY",
                "WEDNESDAY",
                "FRIDAY"));
        workingDayService = new DefaultWorkingDayService(workingDayServiceProperties);
        final LocalDate fromDate = LocalDate.parse("2019-04-03");

        // when
        final WorkingDay workingDay = workingDayService.getNextWorkingDay(fromDate);

        // Then
        assertThat(workingDay.getDate(), Matchers.equalTo(LocalDate.parse("2019-04-05")));
        assertThat(workingDay.getDayOfWeek(), Matchers.equalTo("FRIDAY"));
    }

    //TODO - Resolve current date test
    @Test
    public void shouldGetNextWorkingDayAfterCurrentDateIfNoAfterDateIsProvided() {
        //Given
        String instantExpected = "2019-04-10T10:15:30Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
        Instant.now(clock);

        // when
        final WorkingDay workingDay = workingDayService.getNextWorkingDay(null);

        // Then
        assertThat(workingDay.getDate(), Matchers.equalTo(LocalDate.parse("2019-04-11")));
        assertThat(workingDay.getDayOfWeek(), Matchers.equalTo("THURSDAY"));
    }
}