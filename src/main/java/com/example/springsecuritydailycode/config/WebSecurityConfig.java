package com.example.springsecuritydailycode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@EnableWebSecurity
@Component
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URL ={
            "/hello",
            "/login",
            "/lightweight",
            "/verifyRegistration*",
            "/register"
    };
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .anyRequest().permitAll();
        return http.build();


//                .authorizeHttpRequests(authorize-> {
//                    try {
//                        authorize
//                                .requestMatchers(WHITE_LIST_URL).permitAll()
//                                .anyRequest().permitAll()
//                                .and()
//                                .httpBasic();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//        );
    }
}
