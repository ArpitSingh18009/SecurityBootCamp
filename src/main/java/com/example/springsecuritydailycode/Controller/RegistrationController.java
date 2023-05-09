package com.example.springsecuritydailycode.Controller;

import com.example.springsecuritydailycode.Entity.User;
import com.example.springsecuritydailycode.Entity.VerificationToken;
import com.example.springsecuritydailycode.Event.RegistrationCompleteEvent;
import com.example.springsecuritydailycode.Model.PasswordModel;
import com.example.springsecuritydailycode.Model.ResetPassword;
import com.example.springsecuritydailycode.Model.UserModel;
import com.example.springsecuritydailycode.Services.EmailService;
import com.example.springsecuritydailycode.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid"))
            return "User verify successfully";
        else
            return "Bad User";
    }

//    @GetMapping("/resendVerificationToken")
//    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
//        VerificationToken verificationToken = userService.resendVerificationToken(oldToken, request);
//        User user = verificationToken.getUser();
//        resendVerificationTokenToMail(user, applicationUrl(request), verificationToken);
//        return "Token send to mail";
//    }
//
//    @PostMapping("/resetPassword")
//    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
//        User user = userService.getUserByEmailId(passwordModel.getEmailId());
//        String url = "";
//        if (user != null) {
//            String token = UUID.randomUUID().toString();
//            userService.resetPasswordUsingToken(token, user);
//            url = sendPasswordTokenToMail(user, applicationUrl(request), token);
//        }
//        return url;
//    }

//    @GetMapping("/savePasswordUsingToken")
//    public String verifyToken(@RequestParam("token") String token)
//    {
//
//    }
//    @GetMapping("/savePassword")
//    public String savePassword(@RequestParam("token") String token)
//    {
//        String result = userService.validatePasswordResetToken(token);
//        if(!result.equalsIgnoreCase("valid"))
//        {
//            return "Invalid Token";
//        }
//        // move to next page where it is going to enter new password and repeat password and token
//
//        Optional<User> user = userService.getUserByPasswordResetToken(token);
//        if(user.isPresent())
//        {
//          //  userService.changePassword(user.get(),passwordModel.getNewPassword());
//            return "Reset Password Successful";
//        }
//        else {
//            return "Can't validate the Token";
//        }
//
//    }
//    @PostMapping("/change-password")
//    public String changePassword(@RequestBody PasswordModel passwordModel)
//    {
//        User user = userService.getUserByEmailId(passwordModel.getEmailId());
//        if(!userService.checkIfValidOldPassword(user,passwordModel))
//        {
//            return "Invalid Password";
//        }
//
//        userService.changePassword(user,passwordModel.getNewPassword());
//        return "Password change Sucessfully";
//    }
//    private String sendPasswordTokenToMail(User user, String applicationUrl, String token) {
//        String url = applicationUrl + "/savePassword?token=" + token;
//
//        //emailService.sendEmail(user.getEmailId(),"Reset Password Verification Token",url);
//        // Send verification email
//        log.info("Click the link to reset your password :{}",url);
//
//        return url;
//    }

//    @PostMapping("/send-email")
//    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
//        emailService.sendEmail(to, subject, body);
//        return "Email sent!";
//    }
//    private void resendVerificationTokenToMail(User user, String applicationUrl, VerificationToken verificationToken) {
//        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
//
//        emailService.sendEmail(user.getEmailId(),"Verification Token",url);
//        // Send verification email
//        //log.info("Click the link to verify your account :{}",url);
//    }

    private String applicationUrl(HttpServletRequest request) {

        return "http://" + request.getServerName() +":"+ request.getServerPort() + request.getContextPath();
    }


}
