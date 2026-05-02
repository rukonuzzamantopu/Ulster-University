package org.example;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/** Simple console input/output helper. */
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    public int readIntInRange(String prompt, int min, int max) {
        int value = min - 1;
        while (value < min || value > max) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try { value = Integer.parseInt(line); } catch (NumberFormatException e) { value = min - 1; }
        }
        return value;
    }

    public String readLine() {
        return scanner.nextLine().trim();
    }

    public boolean readYes(String prompt) {
        System.out.print(prompt);
        String ans = scanner.nextLine().trim().toLowerCase();
        return !ans.isEmpty() && ans.charAt(0) == 'y';
    }

    public void promptEnter() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    public void println(String s) { System.out.println(s); }
    public void print(String s)  { System.out.print(s); }

    public String formatCards(List<Card> list) {
        if (list == null || list.isEmpty()) return "(none)";
        return list.stream().map(Card::toString).collect(Collectors.joining(" "));
    }
}
