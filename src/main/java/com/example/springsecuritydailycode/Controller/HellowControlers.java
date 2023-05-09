package com.example.springsecuritydailycode.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HellowControlers {

    @GetMapping("/hello")
    public String hellomethod()
    {
        return "Welcome to SecurityProjecty";
    }
    @GetMapping("/login")
    public String login()
    {
        return "you are in the login page";
    }

}
