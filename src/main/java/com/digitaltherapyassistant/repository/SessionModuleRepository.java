package com.digitaltherapyassistant.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaltherapyassistant.entity.SessionModule;

public interface SessionModuleRepository extends JpaRepository<SessionModule, UUID>{
    
}
