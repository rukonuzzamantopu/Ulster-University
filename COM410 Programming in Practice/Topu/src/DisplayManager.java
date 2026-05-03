package org.example;

import java.util.List;

/**
 * Displays player collections and round scores.
 */
public class DisplayManager {
    private final List<Player> players;

    public DisplayManager(List<Player> players) {
        this.players = players;
    }

    public void execute() {
        System.out.println("*******************************");
        System.out.println("\n--- Collections and Round Scores ---");
        System.out.println("*******************************");
        for (Player p : players) {
            System.out.println(p.GET_NAME() + " - Round points: " + p.GET_ROUND_POINTS());
            System.out.println("Collected:");
            List<Card> col = p.GET_COLLECTED();
            if (col.isEmpty()) {
                System.out.println("(none)");
            } else {
                for (int i = 0; i < col.size(); i++)
                    System.out.println((i + 1) + ": " + col.get(i));
            }
        }
    }

    private String formatCards(List<Card> list) {
        if (list.isEmpty()) return "(none)";
        StringBuilder sb = new StringBuilder();
        for (Card c : list) sb.append(c).append(" ");
        return sb.toString().trim();
    }
}
