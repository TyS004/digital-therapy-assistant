package com.digitaltherapyassistant.service.interfaces;


import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;
import com.digitaltherapyassistant.model.CrisisDetectionResult;
import com.digitaltherapyassistant.service.rag.CrisisDetectionResultDto;

import java.util.List;
import java.util.UUID;

public interface CrisisServiceInterface {

    // get coping strategies
    public List<CopingStrategy> getCopingStrategies() ;

    // get emergency/trusted contacts
    public List<TrustedContact> getTrustedContacts(UUID userId) ;

    // get safety plan
    public SafetyPlan getSafetyPlan(UUID userId) ;

    // update safety plan
    public void updateSafetyPlan(UUID userId, String safetyPlan) ;

    // detect crisis
    public CrisisDetectionResult detectCrisis(UUID userId, String text) ;


}