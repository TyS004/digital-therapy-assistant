package com.digitaltherapyassistant.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Scanner;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.digitaltherapyassistant.cli.api.crisis.CrisisAPIClient;
import com.digitaltherapyassistant.cli.commands.BackCommand;
import com.digitaltherapyassistant.cli.commands.crisis.CopingStrategiesCommand;
import com.digitaltherapyassistant.cli.commands.crisis.CrisisSupportMenuCommand;
import com.digitaltherapyassistant.cli.commands.crisis.EmergencyResourcesCommand;
import com.digitaltherapyassistant.cli.commands.crisis.SafetyPlanCommand;

@ExtendWith(MockitoExtension.class)
public class CrisisMenuTest {
    @Mock private CrisisAPIClient crisisAPIClient;
    @Mock private Scanner in;

    private CopingStrategiesCommand copingStrategiesCommand;
    private EmergencyResourcesCommand emergencyResourcesCommand;
    private SafetyPlanCommand safetyPlanCommand;
    private CrisisSupportMenuCommand crisisSupportMenuCommand;

    @BeforeEach
    void setup() {
        copingStrategiesCommand = new CopingStrategiesCommand(crisisAPIClient);
        emergencyResourcesCommand = new EmergencyResourcesCommand(crisisAPIClient);
        safetyPlanCommand = new SafetyPlanCommand(crisisAPIClient);
        crisisSupportMenuCommand = new CrisisSupportMenuCommand(
            copingStrategiesCommand, emergencyResourcesCommand, safetyPlanCommand, new BackCommand());
    }

    @Test
    public void testCopingStrategiesCommand() {
        assertEquals(true, copingStrategiesCommand.execute(in));
        verify(crisisAPIClient).getCopingStrategies();
    }

    @Test
    public void testEmergencyResourcesCommand() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString());
        assertEquals(true, emergencyResourcesCommand.execute(in));
        verify(crisisAPIClient).getCrisisHub(userId);
    }

    @Test
    public void testSafetyPlanCommand() {
        UUID userId = UUID.randomUUID();
        when(in.nextLine()).thenReturn(userId.toString());
        assertEquals(true, safetyPlanCommand.execute(in));
        verify(crisisAPIClient).getSafetyPlan(userId);
    }

    @Test
    public void testCrisisSupportMenuCommand() {
        when(in.nextLine()).thenReturn("0");
        assertEquals(true, crisisSupportMenuCommand.execute(in));
        assertEquals("5", crisisSupportMenuCommand.getName());
        assertEquals("Crisis Support", crisisSupportMenuCommand.getMenuLabel());
    }

    @Test
    void emergencyResourcesInvalidUserId() {
        when(in.nextLine()).thenReturn("not-uuid");
        assertFalse(emergencyResourcesCommand.execute(in));
        verify(crisisAPIClient, never()).getCrisisHub(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void safetyPlanInvalidUserId() {
        when(in.nextLine()).thenReturn("bad");
        assertFalse(safetyPlanCommand.execute(in));
        verify(crisisAPIClient, never()).getSafetyPlan(org.mockito.ArgumentMatchers.any());
    }
}
