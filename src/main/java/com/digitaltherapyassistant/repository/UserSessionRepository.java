package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

}