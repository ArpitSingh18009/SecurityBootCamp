package com.example.springsecuritydailycode.Services;

import com.example.springsecuritydailycode.Entity.User;
import com.example.springsecuritydailycode.Entity.VerificationToken;
import com.example.springsecuritydailycode.Model.PasswordModel;
import com.example.springsecuritydailycode.Model.ResetEmail;
import com.example.springsecuritydailycode.Model.ResetPassword;
import com.example.springsecuritydailycode.Model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken resendVerificationToken(String oldToken, HttpServletRequest request);

    User getUserByEmailId(String emailId);

    void resetPasswordUsingToken(String token, User user);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, PasswordModel passwordModel);

    String resetPassword(ResetEmail passwordModel, HttpServletRequest request);

    String savePassword(String token);

    String updatePassword(ResetPassword resetPassword, String token);
}
