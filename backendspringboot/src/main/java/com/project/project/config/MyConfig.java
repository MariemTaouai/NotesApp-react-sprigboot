package com.project.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.glassfish.jersey.server.ResourceConfig;

import com.project.project.Service.NotesService;
import com.project.project.Service.UserService;

import java.util.Arrays;

@Configuration
public class MyConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }


    @Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(UserService.class);
        resourceConfig.register(NotesService.class);
        return resourceConfig;
    }
}
