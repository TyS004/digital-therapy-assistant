package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.CbtSession;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    public Optional<UserSession> findByCbtSession(CbtSession session);
    public Optional<List<UserSession>> findAllByUser(User user);
}