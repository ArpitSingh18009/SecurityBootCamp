package com.example.springsecuritydailycode.Services;

import com.example.springsecuritydailycode.Entity.PasswordResetToken;
import com.example.springsecuritydailycode.Entity.User;
import com.example.springsecuritydailycode.Entity.VerificationToken;
import com.example.springsecuritydailycode.Model.PasswordModel;
import com.example.springsecuritydailycode.Model.ResetEmail;
import com.example.springsecuritydailycode.Model.ResetPassword;
import com.example.springsecuritydailycode.Model.UserModel;
import com.example.springsecuritydailycode.Repository.PasswordResetTokenRepository;
import com.example.springsecuritydailycode.Repository.UserRepository;
import com.example.springsecuritydailycode.Repository.VerificationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServicesImplementation implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmailId(userModel.getEmailId());
        user.setRole("USER");

        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(user);
        return user;

    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user,token);
        verificationRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        // fetching the token from database
        VerificationToken verificationToken = verificationRepository.findByToken(token);
        if(verificationToken == null)
            return "invalaid";

        // get user from token by userId
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        // checking time and validating token by time
        if((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime())<=0)
        {
            verificationRepository.delete(verificationToken);
            return "tokenExpired";
        }

        // user verified and enable it
        user.setEnabled(true);

        // save into the database
        userRepository.save(user);
        return "valid";


    }

    @Override
    public VerificationToken resendVerificationToken(String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = verificationRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    @Override
    public void resetPasswordUsingToken(String token, User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);
        //log.info(passwordResetToken.getToken());
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null)
            return "invalid";

        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime())<=0)
        {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "tokenExpired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, PasswordModel passwordModel) {

        return passwordEncoder.matches(user.getPassword(),passwordModel.getOldPassword());
    }

    @Override
    public String resetPassword(ResetEmail passwordModel, HttpServletRequest request) {
        User user = getUserByEmailId(passwordModel.getEmailId());
        String url = "";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            resetPasswordUsingToken(token, user);
            url = sendPasswordTokenToMail(user, applicationUrl(request), token);
        }
        return url;
    }

    @Override
    public String savePassword(String token) {
        String result = validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid"))
        {
            return "Invalid Token";
        }
        // move to next page where it is going to enter new password and repeat password and token

        return null;
    }

    @Override
    public String updatePassword(ResetPassword resetPassword, String token) {
        Optional<User> user = getUserByPasswordResetToken(token);
        if(user != null)
        {
            changePassword(user.get(),resetPassword.getNewPassword());
        }
        return "Password Successfully updated";
    }
    private boolean validatePassword(ResetPassword resetPassword) {
        if(resetPassword.getNewPassword().equals(resetPassword.getRepeatPassword()))
            return true;

        return false;
    }
    private String sendPasswordTokenToMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;

        //emailService.sendEmail(user.getEmailId(),"Reset Password Verification Token",url);
        // Send verification email
        log.info("Click the link to reset your password :{}",url);

        return url;
    }
    private String applicationUrl(HttpServletRequest request) {

        return "http://" + request.getServerName() +":"+ request.getServerPort() + request.getContextPath();
    }
}
