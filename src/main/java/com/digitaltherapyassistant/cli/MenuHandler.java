package com.digitaltherapyassistant.cli;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

public class MenuHandler{
    private Map<String, Command> commandMap;

    public MenuHandler(List<Command> commandList) throws NullPointerException{
        commandMap = commandList.stream()
            .collect(Collectors.toMap(Command::getName, c -> c));
    }

    public void runMenu(String title, Scanner in){
        while(true)
        {
            try { 
                if(!processCommand(title, in)) { break; }
            }
            catch(CommandLineException e) { System.out.println(e.getReason()); }
        }
    }

    public boolean processCommand(String title, Scanner in) throws CommandLineException{
        display(title);

        String input = getInput(in);
        Command command = commandMap.get(input);
        if(command != null){
            if(command.getMenuLabel().equals("Back")) { return false; }
            command.execute(in);
        }
        else{ throw new CommandLineException("Invalid Input, Try Again!"); }

        return true;
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
        System.out.print("Enter choice: ");
        return scanner.nextLine().trim();
    }

    public void invalidOption() {
        System.out.println("Invalid option. Please try again.");
    }
}
