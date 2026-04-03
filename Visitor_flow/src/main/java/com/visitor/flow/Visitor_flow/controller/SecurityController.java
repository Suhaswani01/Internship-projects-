package com.visitor.flow.Visitor_flow.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.visitor.flow.Visitor_flow.entity.VisitorImfo;
import com.visitor.flow.Visitor_flow.service.SecurityService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;



    @GetMapping("/visitor/{id}")
    public ResponseEntity<?> getVisitor(@PathVariable Long id) {
        try {
            VisitorImfo visitor = securityService.getVisitorById(id);
            return ResponseEntity.ok(visitor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        }
    }

   
    
    @PutMapping("/{id}/checkin")
    public ResponseEntity<?> checkIn(@PathVariable Long id) {
        try {
            VisitorImfo updated = securityService.checkIn(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }

    
    @PutMapping("/{id}/checkout")
    public ResponseEntity<?> checkOut(@PathVariable Long id) {
        try {
            VisitorImfo updated = securityService.checkOut(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }
}
