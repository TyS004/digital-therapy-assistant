package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.CognitiveDistortion;
import com.digitaltherapyassistant.entity.DiaryEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, UUID> {

    Page<DiaryEntry> findByUserIdAndDeletedFalseOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    List<DiaryEntry> findByUserIdAndDeletedFalse(UUID userId);

    @Query("SELECT cd, COUNT(cd) as cnt FROM DiaryEntry de " +
           "JOIN de.distortions cd " +
           "WHERE de.user.id = :userId AND de.deleted = false " +
           "GROUP BY cd ORDER BY cnt DESC")
    List<Object[]> findTopDistortionsByUser(@Param("userId") UUID userId);

    @Query("SELECT AVG(de.moodAfter - de.moodBefore) FROM DiaryEntry de " +
           "WHERE de.user.id = :userId AND de.deleted = false " +
           "AND de.moodBefore IS NOT NULL AND de.moodAfter IS NOT NULL")
    Double calculateAverageMoodImprovement(@Param("userId") UUID userId);


    @Query("SELECT dis FROM DiaryEntry d " +
            "JOIN d.distortions dis " +
            "WHERE d.user.id = :userId " +
            "GROUP BY dis " +
            "ORDER BY COUNT(dis) DESC ")
    public Page<CognitiveDistortion> findTopDistortionsByUser(@Param("userId") UUID userId, Pageable pageable);

}
