package org.example;

import java.util.*;

/**
 * Application entry point and coordinator.
 */
public class Main {

    // State
    private final List<Player> PLAYERS = new ArrayList<>();
    private final Deck DECK = new Deck();
    private final Scanner SCANNER = new Scanner(System.in);
    private int ROUNDS = 5;

    // Entry
    public static void main(String[] args) {
        new Main().RUN();
    }

    private void RUN() {
        System.out.println("---Hellow Everyone I am komol here --- ");
        System.out.println("  Its My  High Card Series - Console Game     ");
        System.out.println("  I am happy if you play  Game     ");

        int rounds = new SetupManager(PLAYERS, SCANNER).execute();
        DECK.SHUFFLE();
        new RoundManager(PLAYERS, DECK, SCANNER, rounds).execute();
        new DisplayManager(PLAYERS).execute();
        new AdjustmentManager(PLAYERS, DECK, SCANNER).execute();
        new BonusResultManager(PLAYERS).execute();
    }

    /** Reusable prompt utility used by managers. */
    private int PROMPT_INT(String MSG, int LO, int HI) {
        while (true) {
            System.out.print(MSG);
            try {
                int V = Integer.parseInt(SCANNER.nextLine().trim());
                if (V >= LO && V <= HI) return V;
            } catch (NumberFormatException ignored) { }
        }
    }

    // managers are now top-level classes
}
