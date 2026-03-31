package com.digitaltherapyassistant.controller;

import com.digitaltherapyassistant.dto.request.SafetyPlanRequest;
import com.digitaltherapyassistant.dto.response.*;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;
import com.digitaltherapyassistant.mapper.DtoMapper;
import com.digitaltherapyassistant.model.CrisisDetectionResult;
import com.digitaltherapyassistant.service.CrisisService;
import com.digitaltherapyassistant.service.interfaces.CrisisServiceInterface;
import com.digitaltherapyassistant.service.rag.CrisisDetectionResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.digitaltherapyassistant.dto.response.ApiResponse.success;

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
            @RequestParam(required = true) String userId) {

        SafetyPlan safetyPlan = this.crisisService.getSafetyPlan(String userId) ;
        List<TrustedContact> trustedContacts = this.crisisService.getTrustedContacts(String userId) ;
        List<CopingStrategy> copingStrategies = this.crisisService.getCopingStrategies() ;

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
            @Parameter(description = "Filter By User Id")
            @RequestParam(required = true) String userId
    ) {

        CrisisDetectionResultDto crisisDetectionResult = this.crisisService.detectCrisis(userId) ;

        CrisisDetectionResponse crisisDetectionResponse = this.mapper.toCrisisDetectionResponse(crisisDetectionResult) ;

        return ResponseEntity.ok(ApiResponse.success("Crisis Detection Successful", crisisDetectionResponse)) ;

    }

    @Operation(summary = "Retrieve user safety plan", description = "Retrieve the full safety plan of a user to provide a personalized treatment plan")
    @GetMapping("/safety-plan")
    public ResponseEntity<ApiResponse<SafetyPlanResponse>> getSafetyPlan(
            @Parameter(description = "Filter By User Id")
            @RequestParam(required = true) String userId
    ) {

        SafetyPlan safetyPlan = this.crisisService.getSafetyPlan(userId) ;

        SafetyPlanResponse safetyPlanResponse = this.mapper.toSafetyPlanResponse(safetyPlan) ;

        return ResponseEntity.ok(ApiResponse.success("Safety Plan Retrieved Successfully", safetyPlanResponse)) ;

    }

    @Operation(summary = "Update user safety plan", description = "Update the user safety plan to new user provided information")
    @PutMapping("/safety-plan")
    public ResponseEntity<ApiResponse<SafetyPlanResponse>> updateSafetyPlan(
            @Valid @RequestBody SafetyPlanRequest request) {

        SafetyPlan safetyPlan = this.crisisService.updateSafetyPlan(request.getUserId(), request.getSafetyPlan());

        SafetyPlanResponse safetyPlanResponse = this.mapper.toSafetyPlanResponse(safetyPlan) ;

        return ResponseEntity.ok(ApiResponse.success("Safety Plan Updated Successfully", safetyPlanResponse)) ;
    }







}
