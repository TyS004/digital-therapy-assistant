package com.digitaltherapyassistant;

import com.digitaltherapyassistant.repository.CopingStrategyRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.service.CrisisService;
import com.digitaltherapyassistant.service.rag.CrisisDetector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringBoot Crisis Service – Unit Tests")
public class CrisisServiceTest {

    @Mock
    private UserRepository userRepository ;

    @Mock
    private CopingStrategyRepository copingStrategyRepository ;

    @Mock
    private CrisisDetector crisisDetector ;

    @InjectMocks
    private CrisisService crisisService ;

    private static UUID userId ;

    @BeforeAll
    static void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("getCrisisHub should return the user's crisis hub")
    void getCrisisHub_shouldReturnCrisisHub() {

    }

}
