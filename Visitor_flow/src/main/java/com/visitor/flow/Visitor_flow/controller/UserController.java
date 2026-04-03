package com.visitor.flow.Visitor_flow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.visitor.flow.Visitor_flow.entity.authEntity;
import com.visitor.flow.Visitor_flow.service.UserService;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // ── Login ──
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        try {
            authEntity user = userService.login(body.get("email"), body.get("password"));
            return ResponseEntity.ok(Map.of(
                "role",    user.getRole(),
                "email",   user.getEmail(),
                "name",    user.getName() != null ? user.getName() : "",
                "message", "Login successful"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // ── Forgot Password - Send OTP ──
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            String result = userService.sendOtp(body.get("email"));
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // ── Verify OTP ──
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> body) {
        try {
            String result = userService.verifyOtp(body.get("email"), body.get("otp"));
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // ── Reset Password ──
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String result = userService.resetPassword(body.get("email"), body.get("newPassword"));
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }
}