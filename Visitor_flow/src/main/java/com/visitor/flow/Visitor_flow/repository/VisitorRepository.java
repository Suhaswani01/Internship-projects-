package com.visitor.flow.Visitor_flow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.visitor.flow.Visitor_flow.entity.VisitorImfo;

public interface VisitorRepository extends JpaRepository<VisitorImfo, Long> {
	 List<VisitorImfo> findByStatus(String status);
}