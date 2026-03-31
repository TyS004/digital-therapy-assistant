package com.digitaltherapyassistant.mapper;

import com.digitaltherapyassistant.dto.response.CopingStrategyResponse;
import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import com.digitaltherapyassistant.dto.response.SafetyPlanResponse;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.model.CrisisDetectionResult;
import com.digitaltherapyassistant.service.rag.CrisisDetectionResultDto;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public CopingStrategyResponse toCopingStrategyResponse(CopingStrategy copingStrategy) {

    }

    public CrisisDetectionResult toCrisisDetectionResult(CrisisDetectionResultDto crisisDetectionResultDto) {

    }

    public CrisisDetectionResponse toCrisisDetectionResponse(CrisisDetectionResult crisisDetectionResult) {

    }

    public SafetyPlanResponse toSafetyPlanResponse(SafetyPlan safetyPlan) {

    }


}
