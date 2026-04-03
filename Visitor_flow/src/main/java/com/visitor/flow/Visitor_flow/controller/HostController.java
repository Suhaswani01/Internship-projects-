package com.visitor.flow.Visitor_flow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.visitor.flow.Visitor_flow.entity.VisitorImfo;
import com.visitor.flow.Visitor_flow.service.HostService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/host")
public class HostController {

    @Autowired
    private HostService hostService;

    // GET /host/visitors?department=Engineering
    @GetMapping("/visitors")
    public ResponseEntity<?> getVisitorsByDepartment(@RequestParam String department) {
        try {
            List<VisitorImfo> visitors = hostService.getVisitorsByDepartment(department);
            return ResponseEntity.ok(visitors);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // GET /host/visitors/pending?department=Engineering
    @GetMapping("/visitors/pending")
    public ResponseEntity<?> getPendingByDepartment(@RequestParam String department) {
        try {
            List<VisitorImfo> visitors = hostService.getPendingByDepartment(department);
            return ResponseEntity.ok(visitors);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // GET /host/visitors/today?department=Engineering
    @GetMapping("/visitors/today")
    public ResponseEntity<?> getTodayVisitors(@RequestParam String department) {
        try {
            List<VisitorImfo> visitors = hostService.getTodayVisitorsByDepartment(department);
            return ResponseEntity.ok(visitors);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // PUT /host/{id}/approve
    // Approve karo + automatically QR email jaayegi visitor ko
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveVisitor(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "") String hostEmail) {
        try {
            VisitorImfo updated = hostService.approveByHost(id, hostEmail);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // PUT /host/{id}/reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectVisitor(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "") String hostEmail) {
        try {
            VisitorImfo updated = hostService.rejectByHost(id, hostEmail);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }
}
