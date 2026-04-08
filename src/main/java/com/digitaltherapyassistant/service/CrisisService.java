package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;
import com.digitaltherapyassistant.dto.response.crisis.CrisisHubResponse;
import com.digitaltherapyassistant.dto.response.crisis.SafetyPlanResponse;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.exception.ResourceNotFoundException;
import com.digitaltherapyassistant.mapper.DtoMapper;
import com.digitaltherapyassistant.repository.CopingStrategyRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import com.digitaltherapyassistant.service.rag.CrisisDetector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CrisisService implements CrisisServiceInterface {

    private final CrisisDetector crisisDetector ;
    private final UserRepository userRepository ;
    private final CopingStrategyRepository copingStrategyRepository ;
    private final DtoMapper mapper ;

    public CrisisService(CrisisDetector crisisDetector, UserRepository userRepository, CopingStrategyRepository copingStrategyRepository, DtoMapper mapper) {
        this.crisisDetector = crisisDetector ;
        this.userRepository = userRepository ;
        this.copingStrategyRepository = copingStrategyRepository ;
        this.mapper = mapper ;
    }

    @Override
    public CrisisHubResponse getCrisisHub(UUID userId) {
        List<TrustedContact> trustedContacts = this.getTrustedContacts(userId) ;
        List<CopingStrategy> copingStrategies = this.getCopingStrategies() ;
        SafetyPlanResponse safetyPlanResponse = this.getSafetyPlan(userId) ;

        CrisisHubResponse crisisHub = new CrisisHubResponse() ;
        crisisHub.setTrustedContacts(trustedContacts.stream().map(this.mapper::toTrustedContactResponse).toList());
        crisisHub.setCopingStrategies(copingStrategies.stream().map(this.mapper::toCopingStrategyResponse).toList());
        crisisHub.setSafetyPlan(safetyPlanResponse);

        return crisisHub ;
    }


    @Override
    public List<CopingStrategy> getCopingStrategies() {

        return this.copingStrategyRepository.findAll();

    }

    @Override
    public List<TrustedContact> getTrustedContacts(UUID userId) {

        return this.userRepository.getTrustedContacts(userId);
    }

    @Override
    public SafetyPlanResponse getSafetyPlan(UUID userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString())) ;

        String planText = user.getSafetyPlan();

        return this.mapper.toSafetyPlanResponse(planText) ;

    }

    @Override
    public SafetyPlanResponse updateSafetyPlan(UUID userId, String planText) {

        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString())) ;

        user.setSafetyPlan(planText);
        this.userRepository.save(user) ;

        return this.mapper.toSafetyPlanResponse(planText) ;
    }

    @Override
    public CrisisDetectionResponse detectCrisis(String text) {

        return this.crisisDetector.analyze(text) ;

    }
}
