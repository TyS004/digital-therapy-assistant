package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.CognitiveDistortion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CognitiveDistortionRepository extends JpaRepository<CognitiveDistortion, UUID> {

}