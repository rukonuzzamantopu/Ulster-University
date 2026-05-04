package org.Piyal;

import java.util.*;

/**
 * Computes and applies bonus points to players based on their collected cards.
 *
 * Current implementation is intentionally minimal to avoid changing game
 * behaviour. It provides a placeholder `ApplyBonuses` method that can be
 * extended later to award sequence/suit bonuses.
 */
public class BonusManager {
    /**
     * Apply bonuses to every player in the given game state.
     * Current behaviour: no bonus points are awarded.
     */
    public void ApplyBonuses(GameState state) {
        System.out.println("\n=== Bonus 1: Longest Consecutive Rank Sequence ===");

        // Compute longest sequence for each player
        Map<Player, Integer> sequenceMap = new HashMap<>();
        int bestSequence = 0;
        for (Player p : state.Players) {
            int len = LongestConsecutive(p.GetCollected());
            sequenceMap.put(p, len);
            bestSequence = Math.max(bestSequence, len);
        }

        // Award sequence bonuses (require at least length 2)
        final int SEQ_THRESHOLD = 2;
        if (bestSequence < SEQ_THRESHOLD) {
            System.out.println("No sequence bonuses awarded (no sequence length >= " + SEQ_THRESHOLD + ").");
        } else {
            List<Player> seqWinners = new ArrayList<>();
            for (Map.Entry<Player, Integer> e : sequenceMap.entrySet()) if (e.getValue() == bestSequence) seqWinners.add(e.getKey());
            for (Player w : seqWinners) { w.AddBonusPoints(2); }
            if (!seqWinners.isEmpty()) {
                System.out.print("Winners: ");
                for (int i = 0; i < seqWinners.size(); i++) {
                    System.out.print(seqWinners.get(i).GetName());
                    if (i < seqWinners.size() - 1) System.out.print(", ");
                }
                System.out.println(" (+2 each)");
            }
        }

        System.out.println("\n=== Bonus 2: Highest Count In Any Single Suit ===");

        // Compute suit counts per player
        Map<Player, Integer> suitBest = new HashMap<>();
        Map<Player, Map<Card.Suit, Integer>> allCounts = new HashMap<>();
        for (Player p : state.Players) {
            Map<Card.Suit, Integer> counts = new EnumMap<>(Card.Suit.class);
            for (Card c : p.GetCollected()) counts.merge(c.GetSuit(), 1, Integer::sum);
            int best = counts.values().stream().max(Integer::compareTo).orElse(0);
            suitBest.put(p, best);
            allCounts.put(p, counts);
        }

        int topSuit = suitBest.values().stream().max(Integer::compareTo).orElse(0);
        List<Player> suitWinners = new ArrayList<>();
        for (Map.Entry<Player, Integer> e : suitBest.entrySet()) if (e.getValue() == topSuit) suitWinners.add(e.getKey());

        // Print suit counts
        for (Player p : state.Players) {
            Map<Card.Suit,Integer> counts = allCounts.getOrDefault(p, Collections.emptyMap());
            System.out.print(p.GetName() + ": suit counts = ");
            for (Card.Suit s : Card.Suit.values()) System.out.print(s.toString().charAt(0) + "=" + counts.getOrDefault(s,0) + " ");
            System.out.println();
        }

        if (topSuit == 0) {
            System.out.println("No suit bonuses awarded.");
        } else if (suitWinners.size() == 1) {
            Player winner = suitWinners.get(0);
            winner.AddBonusPoints(5);
            System.out.println(winner.GetName() + " is awarded the Suit bonus (+5 points).");
        } else {
            System.out.print("Suit bonus tied between: ");
            for (int i = 0; i < suitWinners.size(); i++) {
                Player p = suitWinners.get(i);
                p.AddBonusPoints(2);
                System.out.print(p.GetName());
                if (i < suitWinners.size() - 1) System.out.print(", ");
            }
            System.out.println(" (+2 each)");
        }
    }

    // Helper for future use: compute longest consecutive run of ranks (by value)
    private int LongestConsecutive(List<Card> cards) {
        if (cards == null || cards.isEmpty()) return 0;
        TreeSet<Integer> values = new TreeSet<>();
        for (Card c : cards) values.add(c.GetRank().GetValue());
        int best = 1, cur = 1, prev = -100;
        for (int v : values) {
            if (prev + 1 == v) { cur++; }
            else { cur = 1; }
            best = Math.max(best, cur);
            prev = v;
        }
        return best;
    }
}

