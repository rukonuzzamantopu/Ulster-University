package org.Komal;

import java.util.List;
import java.util.Scanner;

/**
 * Handles setup: prompts for players and rounds.
 */
public class SetupManager {
    private final List<Player> players;
    private final Scanner scanner;

    public SetupManager(List<Player> players, Scanner scanner) {
        this.players = players;
        this.scanner = scanner;
    }

    public int execute() {
        int count = promptInt("Enter the number of the participants Min 2 Max 5: ", 2, 5);
        for (int i = 1; i <= count; i++) {
            System.out.print("Enter the participants name  " + i + ":");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "Player" + i;
            players.add(new Player(name));
        }
        return promptInt("Enter the number of rounds you want to play Min 5 Max 10: ", 5, 10);
    }

    private int promptInt(String msg, int lo, int hi) {
        while (true) {
            System.out.print(msg);
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= lo && v <= hi) return v;
            } catch (NumberFormatException ignored) { }
        }
    }
}

