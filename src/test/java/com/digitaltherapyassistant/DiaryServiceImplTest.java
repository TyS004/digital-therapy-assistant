package com.digitaltherapyassistant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.digitaltherapyassistant.dto.DiaryEntryCreate;
import com.digitaltherapyassistant.dto.DiaryEntryDetail;
import com.digitaltherapyassistant.dto.DiaryEntryResponse;
import com.digitaltherapyassistant.dto.DiaryEntrySummary;
import com.digitaltherapyassistant.dto.DiaryInsights;
import com.digitaltherapyassistant.dto.DistortionSuggestion;
import com.digitaltherapyassistant.entity.DiaryEntry;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.exception.DigitalTherapyException;
import com.digitaltherapyassistant.repository.CognitiveDistortionRepository;
import com.digitaltherapyassistant.repository.DiaryEntryRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.service.DiaryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DiaryServiceImplTest {

    @Mock private DiaryEntryRepository diaryEntryRepository;
    @Mock private UserRepository userRepository;
    @Mock private CognitiveDistortionRepository cognitiveDistortionRepository;

    @InjectMocks private DiaryServiceImpl diaryService;

    // --- createEntry ---

    @Test
    void testCreateEntry_happyPath() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        DiaryEntryCreate request = new DiaryEntryCreate();
        request.setSituation("Work stress");
        request.setAutomaticThought("I always fail");
        request.setMoodBefore(3);
        request.setMoodAfter(6);
        request.setBeliefRatingBefore(80);
        request.setBeliefRatingAfter(40);

        DiaryEntry saved = new DiaryEntry();
        saved.setId(UUID.randomUUID());
        saved.setUser(user);
        saved.setSituation(request.getSituation());
        saved.setAutomaticThought(request.getAutomaticThought());
        saved.setMoodBefore(request.getMoodBefore());
        saved.setMoodAfter(request.getMoodAfter());
        saved.setBeliefRatingBefore(request.getBeliefRatingBefore());
        saved.setBeliefRatingAfter(request.getBeliefRatingAfter());
        saved.setCreatedAt(LocalDateTime.now());
        saved.setDeleted(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(diaryEntryRepository.save(any())).thenReturn(saved);

        DiaryEntryResponse result = diaryService.createEntry(userId, request);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("Work stress", result.getSituation());
        verify(diaryEntryRepository).save(any());
    }

    @Test
    void testCreateEntry_withDistortions() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        DiaryEntryCreate request = new DiaryEntryCreate();
        request.setSituation("Social anxiety");
        request.setAutomaticThought("Everyone judges me");
        request.setDistortionIds(List.of("distortion-1"));

        DiaryEntry saved = new DiaryEntry();
        saved.setId(UUID.randomUUID());
        saved.setUser(user);
        saved.setSituation(request.getSituation());
        saved.setAutomaticThought(request.getAutomaticThought());
        saved.setCreatedAt(LocalDateTime.now());
        saved.setDeleted(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cognitiveDistortionRepository.findAllById(any())).thenReturn(List.of());
        when(diaryEntryRepository.save(any())).thenReturn(saved);

        DiaryEntryResponse result = diaryService.createEntry(userId, request);

        assertNotNull(result);
        verify(cognitiveDistortionRepository).findAllById(any());
    }

    @Test
    void testCreateEntry_userNotFound_throws() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        DiaryEntryCreate request = new DiaryEntryCreate();
        request.setSituation("Test");
        request.setAutomaticThought("Test thought");

        assertThrows(DigitalTherapyException.class, () -> diaryService.createEntry(userId, request));
    }

    // --- getEntries ---

    @Test
    void testGetEntries_returnsPage() {
        UUID userId = UUID.randomUUID();

        DiaryEntry entry = new DiaryEntry();
        entry.setId(UUID.randomUUID());
        entry.setSituation("Situation");
        entry.setAutomaticThought("Thought");
        entry.setMoodBefore(4);
        entry.setMoodAfter(7);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setDeleted(false);

        Page<DiaryEntry> page = new PageImpl<>(List.of(entry));
        PageRequest pageable = PageRequest.of(0, 10);

        when(diaryEntryRepository.findByUserIdAndDeletedFalseOrderByCreatedAtDesc(userId, pageable))
                .thenReturn(page);

        Page<DiaryEntrySummary> result = diaryService.getEntries(userId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Situation", result.getContent().get(0).getSituation());
    }

    // --- getEntryDetail ---

    @Test
    void testGetEntryDetail_happyPath() {
        UUID entryId = UUID.randomUUID();
        DiaryEntry entry = new DiaryEntry();
        entry.setId(entryId);
        entry.setSituation("Test situation");
        entry.setAutomaticThought("Test thought");
        entry.setDeleted(false);
        entry.setCreatedAt(LocalDateTime.now());

        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.of(entry));

        DiaryEntryDetail result = diaryService.getEntryDetail(entryId);

        assertNotNull(result);
        assertEquals("Test situation", result.getSituation());
    }

    @Test
    void testGetEntryDetail_notFound_throws() {
        UUID entryId = UUID.randomUUID();
        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class, () -> diaryService.getEntryDetail(entryId));
    }

    @Test
    void testGetEntryDetail_deleted_throws() {
        UUID entryId = UUID.randomUUID();
        DiaryEntry entry = new DiaryEntry();
        entry.setId(entryId);
        entry.setDeleted(true);

        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.of(entry));

        assertThrows(DigitalTherapyException.class, () -> diaryService.getEntryDetail(entryId));
    }

    // --- deleteEntry ---

    @Test
    void testDeleteEntry_happyPath() {
        UUID entryId = UUID.randomUUID();
        DiaryEntry entry = new DiaryEntry();
        entry.setId(entryId);
        entry.setDeleted(false);

        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.of(entry));

        diaryService.deleteEntry(entryId);

        assertTrue(entry.getDeleted());
        verify(diaryEntryRepository).save(entry);
    }

    @Test
    void testDeleteEntry_notFound_throws() {
        UUID entryId = UUID.randomUUID();
        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertThrows(DigitalTherapyException.class, () -> diaryService.deleteEntry(entryId));
    }

    // --- suggestDistortions ---

    @Test
    void testSuggestDistortions_allOrNothing() {
        List<DistortionSuggestion> result = diaryService.suggestDistortions("I always fail at everything");
        assertTrue(result.stream().anyMatch(d -> d.getDistortionId().equals("all-or-nothing")));
    }

    @Test
    void testSuggestDistortions_catastrophizing() {
        List<DistortionSuggestion> result = diaryService.suggestDistortions("This is a disaster and the worst outcome");
        assertTrue(result.stream().anyMatch(d -> d.getDistortionId().equals("catastrophizing")));
    }

    @Test
    void testSuggestDistortions_mindReading() {
        List<DistortionSuggestion> result = diaryService.suggestDistortions("I know they think I'm stupid");
        assertTrue(result.stream().anyMatch(d -> d.getDistortionId().equals("mind-reading")));
    }

    @Test
    void testSuggestDistortions_noKeywords_returnsEmpty() {
        List<DistortionSuggestion> result = diaryService.suggestDistortions("I had a normal day today");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSuggestDistortions_multipleMatches() {
        List<DistortionSuggestion> result = diaryService.suggestDistortions("I always think they know I'm a disaster");
        assertEquals(3, result.size());
    }

    // --- getInsights ---

    @Test
    void testGetInsights_withEntries() {
        UUID userId = UUID.randomUUID();

        DiaryEntry entry = new DiaryEntry();
        entry.setDeleted(false);

        when(diaryEntryRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(List.of(entry));
        when(diaryEntryRepository.calculateAverageMoodImprovement(userId)).thenReturn(2.5);
        when(diaryEntryRepository.findTopDistortionsByUser(userId)).thenReturn(List.of());

        DiaryInsights result = diaryService.getInsights(userId);

        assertNotNull(result);
        assertEquals(1, result.getTotalEntries());
        assertEquals(2.5, result.getAverageMoodImprovement());
        assertTrue(result.getSummary().contains("1 entries"));
    }

    @Test
    void testGetInsights_noEntries_showsEmptyMessage() {
        UUID userId = UUID.randomUUID();

        when(diaryEntryRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(List.of());
        when(diaryEntryRepository.calculateAverageMoodImprovement(userId)).thenReturn(null);
        when(diaryEntryRepository.findTopDistortionsByUser(userId)).thenReturn(List.of());

        DiaryInsights result = diaryService.getInsights(userId);

        assertEquals(0, result.getTotalEntries());
        assertEquals(0.0, result.getAverageMoodImprovement());
        assertTrue(result.getSummary().contains("No diary entries"));
    }
}
