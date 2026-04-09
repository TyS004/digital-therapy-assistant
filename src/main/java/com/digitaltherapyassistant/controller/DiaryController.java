package com.digitaltherapyassistant.controller;

import com.digitaltherapyassistant.dto.*;
import com.digitaltherapyassistant.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
@Slf4j
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "Get Diary Entries", description = "Retrieve Diary Entries filtered by user Id")
    @GetMapping("/entries")
    public ResponseEntity<Page<DiaryEntrySummary>> getEntries(
            @Parameter(description = "filter by user id")
            @RequestParam UUID userId,

            @Parameter(description = "number of pages to load")
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        log.debug("GET /diary/entries userId={}", userId);
        return ResponseEntity.ok(diaryService.getEntries(userId, pageable));
    }

    @Operation(summary = "Create a diary entry", description = "User creates a diary entry")
    @PostMapping("/entries")
    public ResponseEntity<DiaryEntryResponse> createEntry(
            @Parameter(description = "filter by user id")
            @RequestParam UUID userId,

            @Parameter(description = "a diary entry object with information such distortions, emotions, and the entry itself")
            @Valid @RequestBody DiaryEntryCreate request) {

        log.debug("POST /diary/entries userId={}", userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.createEntry(userId, request));
    }

    @Operation(summary = "Get diary entry details", description = "returns all details about a specified diary entry")
    @GetMapping("/entries/{entryId}")
    public ResponseEntity<DiaryEntryDetail> getEntryDetail(
            @Parameter(description = "filter by diary entry id")
            @PathVariable UUID entryId) {
        log.debug("GET /diary/entries/{}", entryId);
        return ResponseEntity.ok(diaryService.getEntryDetail(entryId));
    }

    @Operation(summary = "delete a diary entry", description = "delete a users specified diary entry")
    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deleteEntry(
            @Parameter(description = "filter by diary entry id")
            @PathVariable UUID entryId) {
        log.debug("DELETE /diary/entries/{}", entryId);
        diaryService.deleteEntry(entryId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "get the distortions related to a diary entry", description = "get cognitive distortions from a diary entry")
    @PostMapping("/distortions/suggest")
    public ResponseEntity<List<DistortionSuggestion>> suggestDistortions(
            @RequestBody Map<String, String> body) {

        String thought = body.get("thought");
        if (thought == null || thought.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        log.debug("POST /diary/distortions/suggest thought={}", thought);
        return ResponseEntity.ok(diaryService.suggestDistortions(thought));
    }

    @Operation(summary = "get diary insights", description = "get user trends and insights based on diary entries")
    @GetMapping("/insights")
    public ResponseEntity<DiaryInsights> getInsights(
            @Parameter(description = "filter by user id")
            @RequestParam UUID userId) {
        log.debug("GET /diary/insights userId={}", userId);
        return ResponseEntity.ok(diaryService.getInsights(userId));
    }
}
