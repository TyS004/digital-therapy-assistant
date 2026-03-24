package com.digitaltherapyassistant.controller;

import com.digitaltherapyassistant.dto.response.ApiResponse;
import com.digitaltherapyassistant.dto.response.CrisisResponse;
import com.digitaltherapyassistant.mapper.DtoMapper;
import com.digitaltherapyassistant.service.CrisisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crisis")
@Tag(name="Crisis", description="")
public class CrisisController {

    private final CrisisService crisisService ;
    private final DtoMapper mapper ;

    CrisisController(CrisisService crisisService, DtoMapper mapper) {
        this.crisisService = crisisService ;
        this.mapper = mapper ;
    }

    @Operation(summary = "", description = "")
    @GetMapping()
    public ResponseEntity<ApiResponse<CrisisResponse>> getCrisisHub() {

    }



}
