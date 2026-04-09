package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.CbtSession;
import com.digitaltherapyassistant.entity.User;

import com.digitaltherapyassistant.entity.Status;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.entity.UserSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

    Optional<UserSession> findByCbtSession(CbtSession session);

    Optional<List<UserSession>> findAllByUser(User user);

    List<UserSession> findByUserIdOrderByStartedAtDesc(UUID userId);

    @Query("SELECT us FROM UserSession us " +
            "JOIN FETCH us.cbtSession " +
            "WHERE us.user.id = :userId " +
            "ORDER BY us.startedAt DESC")
    public List<UserSession> findRecentSessionHistoryByUserId(@Param("userId") UUID userId) ;

    @Query("SELECT us FROM UserSession us WHERE us.user.id = :userId AND us.startedAt BETWEEN :start AND :end")
    List<UserSession> findByUserIdAndDateRange(
            @Param("userId") UUID userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.user.id = :userId AND us.status = 'COMPLETED'")
    long countCompletedSessionsByUser(@Param("userId") UUID userId);

    @Query("SELECT COUNT(us) FROM UserSession us " +
            "WHERE us.user.id = :userId " +
            "AND us.status = Status.COMPLETED")
    public Optional<Integer> findCountCompletedSessionsByUserId(@Param("userId") UUID userId) ;

    Optional<UserSession> findFirstByUserIdAndStatusOrderByStartedAtDesc(UUID userId, Status status);
}
