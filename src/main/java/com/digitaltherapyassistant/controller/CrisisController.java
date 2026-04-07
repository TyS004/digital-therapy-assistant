package com.digitaltherapyassistant.controller;

import com.digitaltherapyassistant.dto.request.CrisisDetectionRequest;
import com.digitaltherapyassistant.dto.request.SafetyPlanUpdateRequest;
import com.digitaltherapyassistant.dto.response.*;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;
import com.digitaltherapyassistant.exception.ResourceNotFoundException;
import com.digitaltherapyassistant.mapper.DtoMapper;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/crisis")
@Tag(name="Crises", description="Crisis Management – ")
public class CrisisController {

    private final CrisisServiceInterface crisisService ;
    private final DtoMapper mapper ;

    CrisisController(CrisisServiceInterface crisisService, DtoMapper mapper) {
        this.crisisService = crisisService ;
        this.mapper = mapper ;
    }



    @Operation(summary = "Get the crisis hub", description = "Retrieve safety plan, trusted contacts, and coping strategies filtered by User Id")
    @GetMapping
    public ResponseEntity<ApiResponse<CrisisHubResponse>> getCrisisHub(
            @Parameter(description = "Filter By User Id")
            @RequestParam(required = true) UUID userId) {

        CrisisHubResponse crisisHub = this.crisisService.getCrisisHub(userId) ;

        return ResponseEntity.ok(ApiResponse.success("Crisis Hub Retrieved Successfully", crisisHub)) ;



    }

    @Operation(summary = "Retrieve coping strategies", description = "Retrieve a list of common coping strategies to help use for immediate relief filtered by User Id")
    @GetMapping("/coping-strategies")
    public ResponseEntity<ApiResponse<List<CopingStrategyResponse>>> getCopingStrategies() {

        List<CopingStrategy> copingStrategies = this.crisisService.getCopingStrategies();

        List<CopingStrategyResponse> copingStrategyResponses = copingStrategies.stream().map(this.mapper::toCopingStrategyResponse).toList();

        return ResponseEntity.ok(ApiResponse.success("Coping Strategies Retrieved Successfully", copingStrategyResponses)) ;


    }

    @Operation(summary = "Detect user crisis", description = "Detect if a user is currently in crisis based on their current usage of the application")
    @PostMapping("/detect")
    public ResponseEntity<ApiResponse<CrisisDetectionResponse>> detectCrisis(
            @Parameter(description = "String to analyze for crisis detection")
            @Valid @RequestBody CrisisDetectionRequest crisisDetectionRequest) {

        CrisisDetectionResponse response = this.crisisService.detectCrisis(crisisDetectionRequest.getBody()) ;

        return ResponseEntity.ok(ApiResponse.success("Crisis Detection Successful", response)) ;

    }

    @Operation(summary = "Retrieve user safety plan", description = "Retrieve the full safety plan of a user to provide a personalized treatment plan")
    @GetMapping("/safety-plan")
    public ResponseEntity<ApiResponse<SafetyPlanResponse>> getSafetyPlan(
            @Parameter(description = "Filter By User Id")
            @RequestParam(required = true) UUID userId
    ) {

        SafetyPlanResponse safetyPlan = this.crisisService.getSafetyPlan(userId);
        return ResponseEntity.ok(ApiResponse.success("Safety Plan Retrieved Successfully", safetyPlan));

    }

    @Operation(summary = "Update user safety plan", description = "Update the user safety plan to new user provided information")
    @PutMapping("/safety-plan")
    public ResponseEntity<ApiResponse<SafetyPlanResponse>> updateSafetyPlan(
            @Valid @RequestBody SafetyPlanUpdateRequest request) {


        SafetyPlanResponse safetyPlan = this.crisisService.updateSafetyPlan(request.getUserId(), request.getSafetyPlan());
        return ResponseEntity.ok(ApiResponse.success("Safety Plan Updated Successfully", safetyPlan)) ;


    }







}
