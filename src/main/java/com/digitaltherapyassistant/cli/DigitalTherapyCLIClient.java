package com.digitaltherapyassistant.cli;

import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.digitaltherapyassistant.cli.commands.ExitCommand;
import com.digitaltherapyassistant.cli.commands.auth.AuthenticationMenuCommand;
import com.digitaltherapyassistant.cli.commands.cbt.CBTSessionsMenuCommand;
import com.digitaltherapyassistant.cli.commands.crisis.CrisisSupportMenuCommand;
import com.digitaltherapyassistant.cli.commands.diary.ThoughtDiaryMenuCommand;
import com.digitaltherapyassistant.cli.commands.progress.ProgressDashboardMenuCommand;
import com.digitaltherapyassistant.cli.commands.settings.SettingsCommand;

@Component
@ConditionalOnProperty(name = "cli.enabled", havingValue = "true", matchIfMissing = true)
public class DigitalTherapyCLIClient implements CommandLineRunner{

    private final MenuHandler menuHandler;
    private final Scanner in = new Scanner(System.in);

    public DigitalTherapyCLIClient(
        AuthenticationMenuCommand authCommand,
        CBTSessionsMenuCommand cbtSessionsCommand,
        ThoughtDiaryMenuCommand diaryCommand,
        ProgressDashboardMenuCommand progressCommand,
        CrisisSupportMenuCommand crisisCommand,
        SettingsCommand settingsCommand,
        ExitCommand exitCommand
    ) 
    {
        this.menuHandler = new MenuHandler(
            List.of(authCommand, cbtSessionsCommand, diaryCommand,
            progressCommand, crisisCommand, settingsCommand, exitCommand));
    }

    @Override
    public void run(String... args) throws Exception {
        menuHandler.runMenu("Main Menu", in);
        in.close();
    }
}