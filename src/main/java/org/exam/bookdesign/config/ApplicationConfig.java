package org.exam.bookdesign.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import org.springframework.http.HttpHeaders;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditorAware();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
//        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("https://localhost:5173"));
//        config.setAllowedOrigins(Collections.singletonList("https://147.102.246.150:5173"));
//        config.setAllowedOrigins(Collections.singletonList("https://147.102.246.46:5173"));
//        config.setAllowedOrigins(Collections.singletonList("https://bookinglabs.central.ntua.gr"));

//        config.setAllowedOrigins(Collections.singletonList("https://booking.ntua.gr:5173"));

        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "DELETE",
                "PUT",
                "PATCH",
                "OPTIONS"
        ));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }


    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("dimideka.dimi@gmail.com");
        mailSender.setPassword("dpik izbg jekt wjkb");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
