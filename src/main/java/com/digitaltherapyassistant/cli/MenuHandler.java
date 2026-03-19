package com.digitaltherapyassistant.cli;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

public class MenuHandler{
    private Map<String, Command> commandMap;

    public MenuHandler(List<Command> commandList) {
        commandMap = commandList.stream()
            .collect(Collectors.toMap(Command::getName, c -> c));
    }

    public void runMenu(String title, Scanner in) {
        while(true){
            display(title);

            String input = getInput(in);
            Command command = commandMap.get(input);
            if(command != null){
                if(command.getMenuLabel().equals("Back")
                    || command.getMenuLabel().equals("Exit")){ 
                    break;
                }
                command.execute(in);
            }
            else{
                invalidOption();
            }
        }
    }

    public void display(String title) {
        System.out.println("\n===========================");
        System.out.println("  " + title);
        System.out.println("===========================");
        commandMap.forEach((key, cmd) ->
                System.out.println(key + ". " + cmd.getMenuLabel())
        );
        System.out.println("===========================");
    }

    public String getInput(Scanner scanner) {
        if(commandMap.get("7") != null){
            System.out.print("Enter choice: ");
        }
        else{
            System.out.print("Enter choice (0 to go back): ");
        }
        return scanner.nextLine().trim();
    }

    public void invalidOption() {
        System.out.println("Invalid option. Please try again.");
    }
}
