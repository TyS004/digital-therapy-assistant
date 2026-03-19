package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, UUID> {

}