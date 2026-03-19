package com.digitaltherapyassistant.cli;

import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public interface Command {
    String getName();
    String getMenuLabel();

    public void execute(Scanner in);
}  