package com.digitaltherapyassistant.service;

import com.digitaltherapyassistant.dto.*;
import com.digitaltherapyassistant.entity.CognitiveDistortion;
import com.digitaltherapyassistant.entity.DiaryEntry;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.exception.DigitalTherapyException;
import com.digitaltherapyassistant.repository.CognitiveDistortionRepository;
import com.digitaltherapyassistant.repository.DiaryEntryRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryServiceImpl implements DiaryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final UserRepository userRepository;
    private final CognitiveDistortionRepository cognitiveDistortionRepository;
    private final AiService aiService ;

    @Override
    @Transactional
    public DiaryEntryResponse createEntry(UUID userId, DiaryEntryCreate request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DigitalTherapyException("User not found: " + userId));

        DiaryEntry entry = new DiaryEntry();
        entry.setUser(user);
        entry.setSituation(request.getSituation());
        entry.setAutomaticThought(request.getAutomaticThought());
        entry.setEmotions(request.getEmotions());
        entry.setAlternativeThought(request.getAlternativeThought());
        entry.setMoodBefore(request.getMoodBefore());
        entry.setMoodAfter(request.getMoodAfter());
        entry.setBeliefRatingBefore(request.getBeliefRatingBefore());
        entry.setBeliefRatingAfter(request.getBeliefRatingAfter());
        entry.setCreatedAt(LocalDateTime.now());
        entry.setDeleted(false);

        if (request.getDistortionIds() != null && !request.getDistortionIds().isEmpty()) {
            List<CognitiveDistortion> distortions = cognitiveDistortionRepository
                    .findAllById(request.getDistortionIds());
            entry.setDistortions(distortions);
        }

        DiaryEntry saved = diaryEntryRepository.save(entry);
        log.debug("Created diary entry {} for user {}", saved.getId(), userId);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiaryEntrySummary> getEntries(UUID userId, Pageable pageable) {
        return diaryEntryRepository
                .findByUserIdAndDeletedFalseOrderByCreatedAtDesc(userId, pageable)
                .map(this::toSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public DiaryEntryDetail getEntryDetail(UUID entryId) {
        DiaryEntry entry = diaryEntryRepository.findById(entryId)
                .orElseThrow(() -> new DigitalTherapyException("Diary entry not found: " + entryId));

        if (Boolean.TRUE.equals(entry.getDeleted())) {
            throw new DigitalTherapyException("Diary entry not found: " + entryId);
        }

        return toDetail(entry);
    }

    @Override
    @Transactional
    public void deleteEntry(UUID entryId) {
        DiaryEntry entry = diaryEntryRepository.findById(entryId)
                .orElseThrow(() -> new DigitalTherapyException("Diary entry not found: " + entryId));

        entry.setDeleted(true);
        diaryEntryRepository.save(entry);
        log.debug("Soft-deleted diary entry {}", entryId);
    }

    @Override
    public List<DistortionSuggestion> suggestDistortions(String thought) {

        log.debug("Suggesting distortions for thought: {}", thought);


        return aiService.analyzeThought(thought) ;


    }

    @Override
    @Transactional(readOnly = true)
    public DiaryInsights getInsights(UUID userId) {
        List<DiaryEntry> entries = diaryEntryRepository.findByUserIdAndDeletedFalse(userId);
        int total = entries.size();

        Double avgImprovement = diaryEntryRepository.calculateAverageMoodImprovement(userId);
        double avg = avgImprovement != null ? avgImprovement : 0.0;

        List<Object[]> topRaw = diaryEntryRepository.findTopDistortionsByUser(userId);
        List<DiaryInsights.TopDistortion> topDistortions = topRaw.stream()
                .limit(5)
                .map(row -> {
                    CognitiveDistortion cd = (CognitiveDistortion) row[0];
                    long count = (long) row[1];
                    return new DiaryInsights.TopDistortion(cd.getId(), cd.getName(), (int) count);
                })
                .collect(Collectors.toList());

        String summary = total == 0
                ? "No diary entries yet. Start logging your thoughts to see insights."
                : String.format("You have %d entries with an average mood improvement of %.1f points.", total, avg);

        return new DiaryInsights(total, avg, topDistortions, summary);
    }

    // --- Mapping helpers ---

    private DiaryEntrySummary toSummary(DiaryEntry entry) {
        List<String> distortionNames = entry.getDistortions() == null ? List.of()
                : entry.getDistortions().stream()
                        .map(CognitiveDistortion::getName)
                        .collect(Collectors.toList());

        return new DiaryEntrySummary(
                entry.getId(),
                entry.getSituation(),
                entry.getAutomaticThought(),
                entry.getMoodBefore(),
                entry.getMoodAfter(),
                distortionNames,
                entry.getCreatedAt()
        );
    }

    private DiaryEntryDetail toDetail(DiaryEntry entry) {
        List<DiaryEntryDetail.DistortionSummary> distortions = entry.getDistortions() == null ? List.of()
                : entry.getDistortions().stream()
                        .map(d -> new DiaryEntryDetail.DistortionSummary(d.getId(), d.getName(), d.getDescription()))
                        .collect(Collectors.toList());

        return new DiaryEntryDetail(
                entry.getId(),
                entry.getSituation(),
                entry.getAutomaticThought(),
                entry.getEmotions(),
                distortions,
                entry.getAlternativeThought(),
                entry.getMoodBefore(),
                entry.getMoodAfter(),
                entry.getBeliefRatingBefore(),
                entry.getBeliefRatingAfter(),
                entry.getCreatedAt()
        );
    }

    private DiaryEntryResponse toResponse(DiaryEntry entry) {
        List<DiaryEntryDetail.DistortionSummary> distortions = entry.getDistortions() == null ? List.of()
                : entry.getDistortions().stream()
                        .map(d -> new DiaryEntryDetail.DistortionSummary(d.getId(), d.getName(), d.getDescription()))
                        .collect(Collectors.toList());

        return new DiaryEntryResponse(
                entry.getId(),
                entry.getUser().getId(),
                entry.getSituation(),
                entry.getAutomaticThought(),
                entry.getEmotions(),
                distortions,
                entry.getAlternativeThought(),
                entry.getMoodBefore(),
                entry.getMoodAfter(),
                entry.getBeliefRatingBefore(),
                entry.getBeliefRatingAfter(),
                entry.getCreatedAt()
        );
    }
}
