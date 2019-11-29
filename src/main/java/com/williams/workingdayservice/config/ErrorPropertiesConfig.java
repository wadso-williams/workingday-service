package com.williams.workingdayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Property configuration for all user error response messages displayed by the controller.
 *
 * @author williams.adeho
 */
@Configuration
@PropertySource("classpath:messages/messages.properties")
public class ErrorPropertiesConfig {

	/**
	 * Bean to create a property source place holder configurer
	 *
	 * @return the new created property source place holder configurer
	 */
	@Bean
	public PropertySourcesPlaceholderConfigurer errorPropertiesConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
