package com.williams.workingdayservice;

import com.williams.workingdayservice.dto.WorkingDay;
import com.williams.workingdayservice.service.DefaultWorkingDayService;
import com.williams.workingdayservice.service.WorkingDayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * API tests cases for the working day service controller.
 *
 * NOTE: @WebMvcTest annotation could be use to narrow down the tests
 * to just the web layer. However I have chosen to test the full Spring application with the server.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {WorkingDayServiceApiTest.TestConfig.class})
public class WorkingDayServiceApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnTuesdayNextWorkingDayAfterMondayDateProvided() {
        URI url = UriComponentsBuilder.fromUriString("/next-working-day")
                .queryParam("after", "2019-04-01")
                .build()
                .toUri();
        assertThat(this.restTemplate.getForObject(url, WorkingDay.class).getDayOfWeek()).contains("TUESDAY");
    }

    @Test
    public void shouldReturnWednesdayNextWorkingDayAfterMondayDateProvidedIfTuesdayIsNotBusinessDay() {
        URI url = UriComponentsBuilder.fromUriString("/next-working-day")
                .queryParam("after", "2019-04-01")
                .build()
                .toUri();
        assertThat(this.restTemplate.getForObject(url, WorkingDay.class).getDayOfWeek()).contains("WEDNESDAY");
    }

    @Configuration
    static class TestConfig {
        private WorkingDayServiceProperties workingDayServiceProperties = new WorkingDayServiceProperties();

        @Bean
        public WorkingDayService workingDayService() {
            workingDayServiceProperties.setWorkingDays(Arrays.asList("MONDAY",
                    "WEDNESDAY",
                    "THURSDAY",
                    "FRIDAY"));
            return new DefaultWorkingDayService(workingDayServiceProperties);
        }
    }
}