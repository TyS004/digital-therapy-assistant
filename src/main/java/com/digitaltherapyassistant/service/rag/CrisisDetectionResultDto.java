package com.digitaltherapyassistant.service.rag;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CrisisDetectionResultDto {
    private static enum RiskLevel {
        NONE("None"),
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        CRITICAL("Critical") ;

        private final String displayName ;

        private RiskLevel(String displayName) {this.displayName = displayName ; }

        public String getDisplayName() {return this.displayName ;}

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

        public String getDisplayName() {return this.displayName ;}

        @Override
        public String toString() {return this.displayName ;}
    }

    @Getter
    @Setter
    private RiskLevel riskLevel ;

    @Getter
    @Setter
    private List<String> keywordsDetected ;

    @Getter
    @Setter
    private RecommendedAction recommendedAction ;

    @Getter
    @Setter
    private String reasoning ;


}
