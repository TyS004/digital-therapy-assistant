package com.digitaltherapyassistant.cli.api.diary;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.dto.DiaryEntryCreate;

@Component
public interface DiaryAPIClient {
    public void createEntry(UUID userId, DiaryEntryCreate request);
    public void getEntries(UUID userId, Pageable pageable);
    public void getInsights(UUID userId);
}
