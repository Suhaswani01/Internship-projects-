package com.visitor.flow.Visitor_flow.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.visitor.flow.Visitor_flow.entity.authEntity;

public interface UserRepo extends JpaRepository<authEntity, Long> {
    Optional<authEntity> findByEmail(String email);
}