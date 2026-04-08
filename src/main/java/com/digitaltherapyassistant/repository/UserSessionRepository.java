package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.CbtSession;
import com.digitaltherapyassistant.entity.User;

import com.digitaltherapyassistant.entity.Status;
import com.digitaltherapyassistant.entity.UserSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    public Optional<UserSession> findByCbtSession(CbtSession session);
    public Optional<List<UserSession>> findAllByUser(User user);

    public List<UserSession> findByUserIdOrderByStartedAtDesc(UUID userId) ;

    @Query(value = "SELECT us FROM UserSession us " +
                    "JOIN FETCH us.cbtSession " +
                    "WHERE us.user.id = :userId " +
                    "ORDER BY us.startedAt DESC")
    public List<UserSession> findRecentSessionHistoryByUserId(@Param("userId") UUID userId) ;


    public Optional<Integer> findCountFindByUserIdAndWhereStausIsCompleted(UUID userId) ;

    public Optional<UserSession> findFirstByUserIdAndStatusOrderByStartedAtDesc(UUID userId, Status status);
}