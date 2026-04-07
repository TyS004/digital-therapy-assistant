package com.digitaltherapyassistant.mapper;

import com.digitaltherapyassistant.dto.response.CopingStrategyResponse;
import com.digitaltherapyassistant.dto.response.SafetyPlanResponse;
import com.digitaltherapyassistant.dto.response.TrustedContactResponse;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public CopingStrategyResponse toCopingStrategyResponse(CopingStrategy copingStrategy) {
        CopingStrategyResponse response = new CopingStrategyResponse() ;

        response.setId(copingStrategy.getId());
        response.setName(copingStrategy.getName());
        response.setDescription(copingStrategy.getDescription());
        response.setSteps(copingStrategy.getSteps());

        return response ;
    }

    public TrustedContactResponse toTrustedContactResponse(TrustedContact trustedContact) {
        TrustedContactResponse response = new TrustedContactResponse() ;
        response.setId(trustedContact.getId());
        response.setName(trustedContact.getName());
        response.setPhone(trustedContact.getPhone());
        response.setRelationship(trustedContact.getRelationship());

        return response ;
    }



    public SafetyPlanResponse toSafetyPlanResponse(String safetyPlan) {
        SafetyPlanResponse safetyPlanResponse = new SafetyPlanResponse() ;
        safetyPlanResponse.setSafetyPlan(safetyPlan);

        return safetyPlanResponse ;
    }


}
