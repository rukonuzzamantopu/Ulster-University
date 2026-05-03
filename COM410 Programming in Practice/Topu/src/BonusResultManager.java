package org.example;

import java.util.*;

/**
 * Calculates bonus points and displays final results.
 */
public class BonusResultManager {
    private final List<Player> players;

    public BonusResultManager(List<Player> players) {
        this.players = players;
    }

    public void execute() {
        System.out.println("*******************************");
        System.out.println("\n--- Calculating Bonuses ---");
        System.out.println("*******************************");
        giveSequenceBonus();
        giveSuitBonus();
        showFinalResults();
    }

    private void giveSequenceBonus() {
        int best = 0;
        Map<Player, Integer> lengths = new LinkedHashMap<>();
        for (Player p : players) {
            int len = longestConsecutive(p.GET_COLLECTED());
            lengths.put(p, len);
            best = Math.max(best, len);
            System.out.println(p.GET_NAME() + " longest consecutive length: " + len);
        }
        awardBonus(lengths, best, 5, "sequence");
    }

    private void giveSuitBonus() {
        int best = 0;
        Map<Player, Integer> counts = new LinkedHashMap<>();
        for (Player p : players) {
            int m = maxSuitCount(p.GET_COLLECTED());
            counts.put(p, m);
            best = Math.max(best, m);
            System.out.println(p.GET_NAME() + " best suit count: " + m);
        }
        awardBonus(counts, best, 5, "suit");
    }

    private int maxSuitCount(List<Card> cards) {
        int[] tally = new int[Card.Suit.values().length];
        for (Card c : cards) tally[c.GET_SUIT().ordinal()]++;
        int max = 0;
        for (int t : tally) if (t > max) max = t;
        return max;
    }

    private void awardBonus(Map<Player, Integer> scores, int best, int sole, String label) {
        if (best <= 0) return;
        List<Player> winners = new ArrayList<>();
        for (Map.Entry<Player, Integer> e : scores.entrySet())
            if (e.getValue() == best) winners.add(e.getKey());

        if (winners.size() == 1) {
            winners.get(0).ADD_BONUS_POINTS(sole);
            System.out.println(winners.get(0).GET_NAME() + " gets +" + sole + " " + label + " bonus");
        } else {
            for (Player w : winners) w.ADD_BONUS_POINTS(2);
            System.out.println(label.substring(0,1).toUpperCase() + label.substring(1) + " count tie - each gets +2");
        }
    }

    private int longestConsecutive(List<Card> collected) {
        if (collected.isEmpty()) return 0;
        boolean[] present = new boolean[15]; // indices 2-14
        for (Card c : collected) present[c.GET_RANK().GET_VALUE()] = true;

        int best = 0, cur = 0;
        for (int v = 2; v <= 14; v++) {
            if (present[v]) { cur++; best = Math.max(best, cur); }
            else            { cur = 0; }
        }
        return best;
    }

    private void showFinalResults() {
        System.out.println("*******************************");
        System.out.println("\n--- Final Results ---");
        System.out.println("*******************************");
        players.sort((a, b) -> b.GET_FINAL_SCORE() - a.GET_FINAL_SCORE());

        for (Player p : players)
            System.out.println(p.GET_NAME()
                    + " - Round: "  + p.GET_ROUND_POINTS()
                    + " Bonus: "    + p.GET_BONUS_POINTS()
                    + " Total: "    + p.GET_FINAL_SCORE());

        int topScore = players.get(0).GET_FINAL_SCORE();
        List<Player> topPlayers = new ArrayList<>();
        for (Player p : players)
            if (p.GET_FINAL_SCORE() == topScore) topPlayers.add(p);

        if (topPlayers.size() == 1) {
            System.out.println("Winner: " + topPlayers.get(0).GET_NAME());
        } else {
            System.out.print("Draw between:");
            for (Player p : topPlayers) System.out.print(" " + p.GET_NAME());
            System.out.println();
        }
    }
}
