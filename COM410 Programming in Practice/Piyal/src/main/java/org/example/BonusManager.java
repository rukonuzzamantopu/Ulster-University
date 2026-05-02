package org.example;

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
        // Placeholder: no bonuses awarded. Method left for future enhancements.
        System.out.println("\n###### Calculating Bonuses ######");
        for (Player p : state.Players) {
            // ensure bonus field exists (no change to points)
            p.AddBonusPoints(0);
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

