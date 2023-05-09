package com.example.springsecuritydailycode.Event.Listener;

import com.example.springsecuritydailycode.Entity.User;
import com.example.springsecuritydailycode.Event.RegistrationCompleteEvent;
import com.example.springsecuritydailycode.Services.EmailService;
import com.example.springsecuritydailycode.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the verification link for the user

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);
        // Send mail to the user

        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;


        //emailService.sendEmail(user.getEmailId(),"Verification Token",url);
        // Send verification email
        log.info("Click the link to verify your accoutn :{}",url);
    }
}
