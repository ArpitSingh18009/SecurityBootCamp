package com.example.springsecuritydailycode.Model;

import lombok.Data;

@Data
public class ResetPassword {
    private String newPassword;
    private String repeatPassword;
    private String token;
}
