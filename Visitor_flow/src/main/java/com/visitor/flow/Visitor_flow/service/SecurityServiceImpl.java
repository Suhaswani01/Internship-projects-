package com.visitor.flow.Visitor_flow.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visitor.flow.Visitor_flow.entity.VisitorImfo;
import com.visitor.flow.Visitor_flow.repository.VisitorRepository;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private VisitorRepository visitorRepository;

    // ── Get visitor by ID (for QR scan lookup) ──
    @Override
    public VisitorImfo getVisitorById(Long id) {
        return visitorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Visitor not found: " + id));
    }

    // ── Check In ──
    @Override
    public VisitorImfo checkIn(Long id) {
        VisitorImfo visitor = visitorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Visitor not found: " + id));

        if (!"APPROVED".equalsIgnoreCase(visitor.getStatus())) {
            throw new RuntimeException("Cannot check in — visitor status is: " + visitor.getStatus());
        }

        visitor.setStatus("CHECKED_IN");
        visitor.setCheckInTime(LocalDateTime.now());
        return visitorRepository.save(visitor);
    }

    // ── Check Out ──
    @Override
    public VisitorImfo checkOut(Long id) {
        VisitorImfo visitor = visitorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Visitor not found: " + id));

        if (!"CHECKED_IN".equalsIgnoreCase(visitor.getStatus())) {
            throw new RuntimeException("Cannot check out — visitor status is: " + visitor.getStatus());
        }

        visitor.setStatus("CHECKED_OUT");
        visitor.setCheckOutTime(LocalDateTime.now());
        return visitorRepository.save(visitor);
    }
}