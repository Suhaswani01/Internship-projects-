package com.visitor.flow.Visitor_flow.service;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.visitor.flow.Visitor_flow.entity.authEntity;
import com.visitor.flow.Visitor_flow.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender mailSender;

    // ── Login ──
    @Override
    public authEntity login(String email, String password) {
        authEntity user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    // ── Send OTP ──
    @Override
    public String sendOtp(String email) {
        authEntity user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Email not registered"));

        // 6 digit OTP generate
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        long expiry = System.currentTimeMillis() + (5 * 60 * 1000); // 5 min

        user.setOtp(otp);
        user.setOtpExpiry(expiry);
        userRepo.save(user);

        // Email bhejo
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("VMS — Password Reset OTP");
        msg.setText("Your OTP is: " + otp + "\n\nValid for 5 minutes only.");
        mailSender.send(msg);

        return "OTP sent to " + email;
    }

    // ── Verify OTP ──
    @Override
    public String verifyOtp(String email, String otp) {
        authEntity user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otp.equals(user.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        if (System.currentTimeMillis() > user.getOtpExpiry()) {
            throw new RuntimeException("OTP expired");
        }
        return "OTP verified";
    }

    // ── Reset Password ──
    @Override
    public String resetPassword(String email, String newPassword) {
        authEntity user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepo.save(user);
        return "Password reset successful";
    }
}