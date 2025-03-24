package com.myproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // In more details, this annotation indicates that a class declares one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime
@EnableSpringDataWebSupport // In more details, this annotation registers a PageableHandlerMethodArgumentResolver bean which is used to resolve Pageable method arguments in the controller
public class WebConfiguration implements WebMvcConfigurer {
    // Spring Data Web support is enabled by the annotation
    // No need to manually configure the resolver as it's already provided
}