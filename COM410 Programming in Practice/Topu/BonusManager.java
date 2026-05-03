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
        // Implemented bonus rules:
        // - Run bonus: +3 points for longest consecutive run if length >= 3
        // - Suit bonus: +2 points if player has >=4 cards in a single suit
        // - Four-of-a-kind: +5 points for any rank with 4 or more cards
        System.out.println("\n###### Calculating Bonuses ######");
        for (Player p : state.Players) {
            List<Card> cards = p.GetCollected();
            int bonus = 0;
            int before = p.GetBonusPoints();

            // Four-of-a-kind bonus
            Map<Card.Rank, Integer> rankCounts = new EnumMap<>(Card.Rank.class);
            for (Card c : cards) rankCounts.merge(c.GetRank(), 1, Integer::sum);
            for (Map.Entry<Card.Rank, Integer> e : rankCounts.entrySet()) {
                if (e.getValue() >= 4) {
                    bonus += 5;
                    System.out.println(p.GetName() + " four-of-a-kind bonus (+5) for rank " + e.getKey());
                }
            }

            // Suit bonus (most cards in a single suit)
            Map<Card.Suit, Integer> suitCounts = new EnumMap<>(Card.Suit.class);
            for (Card c : cards) suitCounts.merge(c.GetSuit(), 1, Integer::sum);
            int maxSuit = 0; Card.Suit bestSuit = null;
            for (Map.Entry<Card.Suit, Integer> e : suitCounts.entrySet()) {
                if (e.getValue() > maxSuit) { maxSuit = e.getValue(); bestSuit = e.getKey(); }
            }
            if (maxSuit >= 4) {
                bonus += 2;
                System.out.println(p.GetName() + " suit bonus (+2) for " + bestSuit + " count=" + maxSuit);
            }

            // Run bonus (longest consecutive ranks)
            int run = LongestConsecutive(cards);
            if (run >= 3) {
                bonus += 3;
                System.out.println(p.GetName() + " run bonus (+3) for run length=" + run);
            }

            if (bonus > 0) p.AddBonusPoints(bonus);
            else p.AddBonusPoints(0); // keep behaviour explicit

            // Debug: show computed bonus and player's collected cards
            StringBuilder sb = new StringBuilder();
            sb.append("Collected:");
            for (Card c : cards) sb.append(' ').append(c.toString());
            System.out.println(p.GetName() + " -> " + sb.toString());
            System.out.println(p.GetName() + " bonus computed=" + bonus + " before=" + before + " after=" + p.GetBonusPoints());
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

