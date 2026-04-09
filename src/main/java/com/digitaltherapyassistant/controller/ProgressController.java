package com.digitaltherapyassistant.controller;

import com.digitaltherapyassistant.dto.Achievement;
import com.digitaltherapyassistant.dto.BurnoutRecovery;
import com.digitaltherapyassistant.dto.MonthlyTrends;
import com.digitaltherapyassistant.dto.WeeklySummary;
import com.digitaltherapyassistant.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@Slf4j
public class ProgressController {

    private final ProgressService progressService;

    @Operation(summary="get weekly progress summary", description="get a users weekly progress determined by their usage activity")
    @GetMapping("/weekly")
    public ResponseEntity<WeeklySummary> getWeeklySummary(
            @Parameter(description = "filter by user id")
            @RequestParam UUID userId) {
        log.debug("GET /progress/weekly userId={}", userId);
        return ResponseEntity.ok(progressService.getWeeklySummary(userId));
    }

    @Operation(summary = "get monthly summary", description = "get a users monthly progress determined by their usage activity")
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyTrends> getMonthlyTrends(
            @Parameter(description = "filter by user id")
            @RequestParam UUID userId) {
        log.debug("GET /progress/monthly userId={}", userId);
        return ResponseEntity.ok(progressService.getMonthlyTrends(userId));
    }

    @Operation(summary = "get burnout recovery progress", description = "get a users burnout recovery progress determined by their usage activity")
    @GetMapping("/burnout")
    public ResponseEntity<BurnoutRecovery> getBurnoutRecovery(
            @Parameter(description = "filter by user id")
            @RequestParam UUID userId) {
        log.debug("GET /progress/burnout userId={}", userId);
        return ResponseEntity.ok(progressService.getBurnoutRecovery(userId));
    }

    @Operation(summary = "get achievements", description = "get all achievements available to a user to earn")
    @GetMapping("/achievements")
    public ResponseEntity<List<Achievement>> getAchievements(
            @Parameter(description = "filter by user id")
            @RequestParam UUID userId) {
        log.debug("GET /progress/achievements userId={}", userId);
        return ResponseEntity.ok(progressService.getAchievements(userId));
    }
}
