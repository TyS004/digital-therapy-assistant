package com.digitaltherapyassistant.controller;

import com.digitaltherapyassistant.dto.response.ApiResponse;
import com.digitaltherapyassistant.dto.response.CopingStrategyResponse;
import com.digitaltherapyassistant.dto.response.CrisisDetectionResponse;
import com.digitaltherapyassistant.dto.response.TrustedContactResponse;
import com.digitaltherapyassistant.mapper.DtoMapper;
import com.digitaltherapyassistant.service.CrisisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crisis")
@Tag(name="Crises", description="Crisis Management – ")
public class CrisisController {

    // get coping strategies
    // I wonder if I should source the coping strategies from the knowledge base?

    // get emergency/trusted contacts

    // get safety plan
    // I wonder if the safety plan should also implement this

    // update safety plan

    // detect crisis

    private final CrisisService crisisService ;
    private final DtoMapper mapper ;

    CrisisController(CrisisService crisisService, DtoMapper mapper) {
        this.crisisService = crisisService ;
        this.mapper = mapper ;
    }

    @Operation(summary = "", description = "")
    @GetMapping
    public ResponseEntity<ApiResponse<TrustedContactResponse>> getCrisisHub() {

    }

    @Operation(summary = "", description = "")
    @GetMapping("/coping-strategies")
    public ResponseEntity<ApiResponse<List<CopingStrategyResponse>>> getCopingStrategies() {

    }

    @Operation(summary = "", description = "")
    @PostMapping("/detect")
    public ResponseEntity<ApiResponse<CrisisDetectionResponse>> detectCrisis() {

    }

    @Operation(summary = "", description =  = "")
    @GetMapping("/safety-plan")
    public ResponseEntity<ApiResponse<SafetyPlanResponse>> getSafetyPlan() {

    }





}
