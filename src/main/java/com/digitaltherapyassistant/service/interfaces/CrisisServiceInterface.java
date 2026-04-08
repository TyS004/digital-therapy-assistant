package com.digitaltherapyassistant.service.interfaces;


import com.digitaltherapyassistant.dto.response.crisis.CrisisDetectionResponse;
import com.digitaltherapyassistant.dto.response.crisis.CrisisHubResponse;
import com.digitaltherapyassistant.dto.response.crisis.SafetyPlanResponse;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;

import java.util.List;
import java.util.UUID;

public interface CrisisServiceInterface {

    public CrisisHubResponse getCrisisHub(UUID userId) ;

    // get coping strategies
    public List<CopingStrategy> getCopingStrategies() ;

    // get emergency/trusted contacts
    public List<TrustedContact> getTrustedContacts(UUID userId) ;

    // get safety plan
    public SafetyPlanResponse getSafetyPlan(UUID userId) ;

    // update safety plan
    public SafetyPlanResponse updateSafetyPlan(UUID userId, String safetyPlan) ;


    // detect crisis
    public CrisisDetectionResponse detectCrisis(String text) ;


}