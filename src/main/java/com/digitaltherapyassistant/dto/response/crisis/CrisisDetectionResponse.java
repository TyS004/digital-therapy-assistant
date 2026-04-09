package com.digitaltherapyassistant.dto.response.crisis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CrisisDetectionResponse {
    public static enum RiskLevel {
        @JsonProperty("none") NONE("None"),
        @JsonProperty("low") LOW("Low"),
        @JsonProperty("medium") MEDIUM("Medium"),
        @JsonProperty("high") HIGH("High"),
        @JsonProperty("critical") CRITICAL("Critical") ;

        private final String displayName ;

        private RiskLevel(String displayName) {this.displayName = displayName ; }

        @Override
        public String toString() {return this.displayName ;}

    }

    public static enum RecommendedAction {
        @JsonProperty("none") NONE("None"),
        @JsonProperty("show_resources") SHOW_RESOURCES("Show Resources"),
        @JsonProperty("show_crisis_hub") SHOW_CRISIS_HUB("Show Crisis Hub"),
        @JsonProperty("immediate_intervention") IMMEDIATE_INTERVENTION("Immediate Intervention") ;

        private final String displayName ;

        private RecommendedAction(String displayName) {this.displayName = displayName ; }

        @Override
        public String toString() {return this.displayName ;}
    }

    @NotBlank(message = "riskLevel required")
    private RiskLevel riskLevel ;

    @NotBlank(message = "detected keywords required")
    private List<String> keywordsDetected ;

    @NotBlank(message = "recommended action required")
    private RecommendedAction recommendedAction ;

    @NotBlank(message = "reasoning required")
    private String reasoning ;

    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }                                                                   
    public void setRecommendedAction(RecommendedAction recommendedAction) { this.recommendedAction = recommendedAction; }  
    public void setKeywordsDetected(List<String> keywordsDetected) { this.keywordsDetected = keywordsDetected; }                                                                   
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }  
    

}
