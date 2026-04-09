package com.digitaltherapyassistant.dto.response.session;

import java.util.List;
import java.util.UUID;

import com.digitaltherapyassistant.entity.Modality;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDetail {
    private UUID id;
    private String title;
    private String description;
    private int durationMinutes;
    private List<String> objectives;
    private List<Modality> modalities;
    private int orderIndex;
    private String message;
}
