package com.digitaltherapyassistant.service.interfaces;


import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;

import java.util.List;
import java.util.UUID;

public interface CrisisServiceInterface {

    // get coping strategies
    public List<CopingStrategy> getCopingStrategies() ;

    // get emergency/trusted contacts
    public List<TrustedContact> getTrustedContacts(UUID userId) ;

    // get safety plan
    public SafetyPlanDto getSafetyPlan(UUID userId) ;

    // update safety plan
    public SafetyPlanDto updateSafetyPlan(UUID userId, String safetyPlan) ;

    // detect crisis
    public CrisisDetectionResponse detectCrisis(String text) ;


}