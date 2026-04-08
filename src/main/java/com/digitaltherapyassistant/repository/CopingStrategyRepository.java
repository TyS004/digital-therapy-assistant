package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.CopingStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CopingStrategyRepository extends JpaRepository<CopingStrategy, UUID> {
}
