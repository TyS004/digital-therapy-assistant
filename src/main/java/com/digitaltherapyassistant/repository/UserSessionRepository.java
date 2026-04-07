package com.digitaltherapyassistant.repository;


import com.digitaltherapyassistant.entity.Status;
import com.digitaltherapyassistant.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

    public List<UserSession> findByUserIdOrderByStartedAtDescLimit3(UUID userId) ;

//    List<U>

    public Optional<Integer> findCountFindByUserIdAndWhereStausIsCompleted(UUID userId) ;

    public Optional<UserSession> findFirstByUserIdAndStatusOrderByStartedAtDesc(UUID userId, Status status);
}