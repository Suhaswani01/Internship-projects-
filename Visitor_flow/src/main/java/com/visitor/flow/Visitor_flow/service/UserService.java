package com.visitor.flow.Visitor_flow.service;

import com.visitor.flow.Visitor_flow.entity.authEntity;

public interface UserService {
    authEntity login(String email, String password);
    String sendOtp(String email);
    String verifyOtp(String email, String otp);
    String resetPassword(String email, String newPassword);
}