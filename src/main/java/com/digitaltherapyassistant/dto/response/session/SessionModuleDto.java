package com.digitaltherapyassistant.dto.response.session;

import com.digitaltherapyassistant.entity.SessionModule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionModuleDto {
    private SessionModule module;
    private String message;
}
