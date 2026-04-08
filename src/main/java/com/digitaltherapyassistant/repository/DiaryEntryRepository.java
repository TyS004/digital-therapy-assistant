package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.CognitiveDistortion;
import com.digitaltherapyassistant.entity.DiaryEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, UUID> {

    @Query(value = "SELECT dis FROM DiaryEntry d " +
                    "JOIN d.distortions dis " +
                    "WHERE d.user.id = :userId " +
                    "GROUP BY dis " +
                    "ORDER BY COUNT(dis) DESC ")
    public Page<CognitiveDistortion> findTopDistortionsByUser(@Param("userId") UUID userId, Pageable pageable) ;
}