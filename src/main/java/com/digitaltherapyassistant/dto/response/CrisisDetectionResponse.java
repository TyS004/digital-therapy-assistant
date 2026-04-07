package com.digitaltherapyassistant.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class CrisisDetectionResponse {
    private static enum RiskLevel {
        NONE("None"),
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        CRITICAL("Critical") ;

        private final String displayName ;

        private RiskLevel(String displayName) {this.displayName = displayName ; }

        @Override
        public String toString() {return this.displayName ;}

    }

    private static enum RecommendedAction {
        NONE("None"),
        SHOW_RESOURCES("Show Resources"),
        SHOW_CRISIS_HUB("Show Crisis Hub"),
        IMMEDIATE_INTERVENTION("Immediate Intervention") ;

        private final String displayName ;

        private RecommendedAction(String displayName) {this.displayName = displayName ; }

        @Override
        public String toString() {return this.displayName ;}
    }

    private RiskLevel riskLevel ;
    private List<String> keywordsDetected ;
    private RecommendedAction recommendedAction ;
    private String reasoning ;

}
