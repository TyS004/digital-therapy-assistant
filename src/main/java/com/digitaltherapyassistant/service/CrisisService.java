package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.mapper.DtoMapper;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import com.digitaltherapyassistant.service.rag.CrisisDetector;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public List<TrustedContact> getTrustedContacts(UUID userId) throws DatabaseException {
        User user = this.userRepository.findById(userId).orElse(null) ;

        try {
            return user.getTrustedContacts();
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException() ;
        }
    }

    @Override
    public SafetyPlanDto getSafetyPlan(UUID userId) throws DatabaseException {
        User user = this.userRepository.findById(userId).orElse(null) ;

        try {
            String planText = user.getSafetyPlan();
            List<TrustedContact> trustedContacts = user.getTrustedContacts();

            return new SafetyPlanDto(planText, trustedContacts);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException() ;
        }
    }

    @Override
    public SafetyPlanDto updateSafetyPlan(UUID userId, String planText) throws DatabaseException {

        User user = this.userRepository.findById(userId).orElse(null) ;

        try {
            user.setSafetyPlan(safetyPlan);

            List<TrustedContact> trustedContacts = user.getTrustedContacts();

            return new SafetyPlanDto(planText, trustedContacts);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException() ;
        }

    }

    @Override
    public CrisisDetectionResponse detectCrisis(String text) throws CrisisDetectionException {

        try {
            return this.crisisDetector.analyze(text);
        } catch (CrisisDetectionException e) {
            throw new CrisisDetectionException() ;
        }
    }
}
