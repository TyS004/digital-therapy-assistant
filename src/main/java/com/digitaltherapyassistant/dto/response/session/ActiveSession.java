package com.digitaltherapyassistant.dto.response.session;

import com.digitaltherapyassistant.entity.UserSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveSession {
    UserSession session;
    String message;
}
