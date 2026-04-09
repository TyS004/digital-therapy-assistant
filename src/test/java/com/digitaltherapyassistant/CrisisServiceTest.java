package com.digitaltherapyassistant;

import com.digitaltherapyassistant.dto.request.crisis.SafetyPlanUpdateRequest;
import com.digitaltherapyassistant.dto.response.crisis.*;
import com.digitaltherapyassistant.entity.CopingStrategy;
import com.digitaltherapyassistant.entity.TrustedContact;
import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.mapper.DtoMapper;
import com.digitaltherapyassistant.repository.CopingStrategyRepository;
import com.digitaltherapyassistant.repository.UserRepository;
import com.digitaltherapyassistant.service.CrisisService;
import com.digitaltherapyassistant.service.rag.CrisisDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringBoot Crisis Service – Unit Tests")
public class CrisisServiceTest {

    @Mock
    private UserRepository userRepository ;

    @Mock
    private CopingStrategyRepository copingStrategyRepository ;

    @Mock
    private CrisisDetector crisisDetector ;

    @Mock
    private DtoMapper mapper ;

    @InjectMocks
    private CrisisService crisisService ;

    private UUID userId ;
    private User mockUser ;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID() ;
        mockUser = new User() ;
        mockUser.setId(userId) ;
        mockUser.setSafetyPlan("Call Therapist") ;
    }

    @Test
    @DisplayName("getCrisisHub should return the user's crisis hub")
    void getCrisisHub_shouldReturnCrisisHub() {

        // Arrange Coping Strategies
        List<CopingStrategy> copingStrategies = new ArrayList<>() ;
        CopingStrategy strategy = new CopingStrategy();
        copingStrategies.add(strategy) ;
        when(copingStrategyRepository.findAll()).thenReturn(copingStrategies) ;
        when(mapper.toCopingStrategyResponse(any(CopingStrategy.class))).thenReturn(new CopingStrategyResponse()) ;

        // Arrange Trusted Contacts
        List<TrustedContact> trustedContacts = new ArrayList<>() ;
        TrustedContact contact = new TrustedContact() ;
        trustedContacts.add(contact) ;
        when(userRepository.getTrustedContacts(any(UUID.class))).thenReturn(trustedContacts) ;
        when(mapper.toTrustedContactResponse(any(TrustedContact.class))).thenReturn(new TrustedContactResponse()) ;

        // Arrange Safety Plan
        SafetyPlanResponse safetyPlan = new SafetyPlanResponse() ;
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockUser)) ;
        when(mapper.toSafetyPlanResponse(any(String.class))).thenReturn(new SafetyPlanResponse()) ;

        // Act
        CrisisHubResponse result = crisisService.getCrisisHub(mockUser.getId()) ;

        // Assert
        assertNotNull(result);
        assertThat(result.getTrustedContacts().size()).isEqualTo(trustedContacts.size()) ;
        assertThat(result.getCopingStrategies().size()).isEqualTo(copingStrategies.size()) ;
        assertNotNull(result.getSafetyPlan()) ;


    }

    @Test
    @DisplayName("getCopingStrategies should return a list of generic coping strategis")
    void getCopingStrategies_shouldReturnStrategies() {
        // Arrange
        List<CopingStrategy> copingStrategies = new ArrayList<>() ;
        CopingStrategy strategy = new CopingStrategy() ;
        copingStrategies.add(strategy) ;

        // Act
        when(copingStrategyRepository.findAll()).thenReturn(copingStrategies) ;

        List<CopingStrategy> result = crisisService.getCopingStrategies() ;

        // Assert
        assertNotNull(result) ;
        assertThat(result.size()).isEqualTo(copingStrategies.size()) ;
        verify(copingStrategyRepository).findAll() ;

    }

    @Test
    @DisplayName("getTrustedContacts should return a list of the users trusted contacts")
    void getTrustedContacts_shouldReturnContacts() {

        List<TrustedContact> trustedContacts = new ArrayList<>() ;
        TrustedContact contact = new TrustedContact() ;
        trustedContacts.add(contact) ;

        when(userRepository.getTrustedContacts(any(UUID.class))).thenReturn(trustedContacts) ;

        List<TrustedContact> result = crisisService.getTrustedContacts(userId) ;

        assertNotNull(result) ;
        assertThat(result.size()).isEqualTo(trustedContacts.size()) ;
        verify(userRepository).getTrustedContacts(any(UUID.class)) ;
    }

    @Test
    @DisplayName("getSafetyPlan should return a users safety plan")
    void getSafetyPlan_shouldReturnSafetyPlan() {

        SafetyPlanResponse safetyPlanResponse = new SafetyPlanResponse() ;
        safetyPlanResponse.setSafetyPlan(mockUser.getSafetyPlan());

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockUser)) ;
        when(mapper.toSafetyPlanResponse(any(String.class))).thenReturn(safetyPlanResponse) ;

        SafetyPlanResponse result = crisisService.getSafetyPlan(userId) ;

        assertNotNull(result) ;
        assertThat(result.getSafetyPlan()).isEqualTo(mockUser.getSafetyPlan()) ;
        verify(userRepository).findById(any(UUID.class)) ;
    }

    @Test
    @DisplayName("updateSafetyPlan should update a users safety plan and return a safety plan object")
    void updateSafetyPlan_shouldUpdateAndReturnSafetyPlan() {


        SafetyPlanUpdateRequest request = new SafetyPlanUpdateRequest() ;
        request.setUserId(mockUser.getId());
        request.setSafetyPlan("New Safety Plan");

        SafetyPlanResponse safetyPlanResponse = new SafetyPlanResponse() ;
        safetyPlanResponse.setSafetyPlan(request.getSafetyPlan());
        when(mapper.toSafetyPlanResponse(any(String.class))).thenReturn(safetyPlanResponse) ;


        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockUser)) ;

        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0) ;
            u.setSafetyPlan(request.getSafetyPlan());
            return u;
        }) ;

        SafetyPlanResponse result = crisisService.updateSafetyPlan(request.getUserId(), request.getSafetyPlan()) ;

        assertNotNull(result) ;
        assertThat(result.getSafetyPlan()).isEqualTo("New Safety Plan") ;
        verify(userRepository).save(any(User.class)) ;
    }

    @Test
    @DisplayName("detectCrisis should return a CrisisDetectionResponse")
    void detectCrisis_shouldReturnCrisisDetection() {

        String sampleText = "Sample" ;

        CrisisDetectionResponse response = new CrisisDetectionResponse() ;

        when(crisisDetector.analyze(any(String.class))).thenReturn(response) ;

        CrisisDetectionResponse result = crisisService.detectCrisis(sampleText) ;

        assertNotNull(result);
    }



}
