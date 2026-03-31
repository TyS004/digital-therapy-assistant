package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;
import com.digitaltherapyassistant.mapper.DtoMapper;
import com.digitaltherapyassistant.model.CrisisDetectionResult;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import com.digitaltherapyassistant.service.rag.CrisisDetectionResultDto;
import com.digitaltherapyassistant.service.rag.CrisisDetector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CrisisService implements CrisisServiceInterface {

    private final CrisisDetector crisisDetector ;
    private final UserRepository userRepository ;
    private final DtoMapper mapper ;

    public CrisisService(CrisisDetector crisisDetector, UserRepository userRepository, DtoMapper mapper) {
        this.crisisDetector = crisisDetector ;
        this.userRepository = userRepository ;
        this.mapper = mapper ;
    }


    @Override
    public List<CopingStrategy> getCopingStrategies() {
        return List.of();
    }

    @Override
    public List<TrustedContact> getTrustedContacts(UUID userId) {
        return List.of();
    }

    @Override
    public SafetyPlan getSafetyPlan(UUID userId) {
        return null;
    }

    @Override
    public void updateSafetyPlan(UUID userId, String safetyPlan) {

    }

    @Override
    public CrisisDetectionResult detectCrisis(UUID userId, String text) {
        CrisisDetectionResultDto resultDto = this.crisisDetector.analyze(text) ;

        CrisisDetectionResult result = this.mapper.toCrisisDetectionResult(resultDto) ;

        return result ;
    }
}
