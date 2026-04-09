package com.digitaltherapyassistant.dto.response.session;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionModuleDto {
    private String id;
    private List<SessionDto> sessions;
    private String message;
}
