package com.digitaltherapyassistant.dto.response.session;

import com.digitaltherapyassistant.entity.CbtSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDetail {
    private CbtSession session;
    private String message;
}
