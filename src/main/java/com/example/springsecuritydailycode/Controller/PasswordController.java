package com.example.springsecuritydailycode.Controller;

import com.example.springsecuritydailycode.Entity.User;
import com.example.springsecuritydailycode.Model.PasswordModel;
import com.example.springsecuritydailycode.Model.ResetEmail;
import com.example.springsecuritydailycode.Model.ResetPassword;
import com.example.springsecuritydailycode.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/pass")
public class PasswordController {

    @Autowired
    private UserService userService;
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody ResetEmail resetEmail, HttpServletRequest request) {
        return userService.resetPassword(resetEmail,request);
    }
    @GetMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token)
    {
        return userService.savePassword(token);
    }
    @PostMapping("/update-password")
    public String updatePassword(@RequestBody ResetPassword resetPassword, @RequestParam("token") String token)
    {
        return userService.updatePassword(resetPassword,token);
    }
}
