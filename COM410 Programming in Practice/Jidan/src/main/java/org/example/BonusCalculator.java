package org.example;

import java.util.*;
import java.util.stream.Collectors;

/** Computes and applies bonus points for players. */
public class BonusCalculator {

    public static void applyBonuses(List<Player> players) {
        applySequenceBonus(players);
        applySuitBonus(players);
    }

    private static void applySequenceBonus(List<Player> players) {
        Map<Player, Integer> seqLengths = new LinkedHashMap<>();
        players.forEach(player -> {
            int length = longestConsecutive(player.getCollected());
            seqLengths.put(player, length);
            System.out.println(player.getName() + " longest consecutive length: " + length);
        });

        int bestLength = seqLengths.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        List<Player> winners = seqLengths.entrySet().stream()
                .filter(e -> e.getValue() == bestLength && bestLength > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (winners.isEmpty()) return;
        if (winners.size() == 1) {
            winners.get(0).addBonusPoints(5);
            System.out.println(winners.get(0).getName() + " gets +5 sequence bonus");
        } else {
            winners.forEach(p -> p.addBonusPoints(2));
            System.out.println("Sequence tie - each gets +2");
        }
    }

    private static void applySuitBonus(List<Player> players) {
        Map<Player, Integer> suitBest = new LinkedHashMap<>();
        players.forEach(player -> {
            int maxSuitCount = Arrays.stream(Card.Suit.values())
                    .mapToInt(s -> (int) player.getCollected().stream().filter(c -> c.getSuit() == s).count())
                    .max().orElse(0);
            suitBest.put(player, maxSuitCount);
            System.out.println(player.getName() + " best suit count: " + maxSuitCount);
        });

        int bestCount = suitBest.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        List<Player> winners = suitBest.entrySet().stream()
                .filter(e -> e.getValue() == bestCount && bestCount > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (winners.isEmpty()) return;
        if (winners.size() == 1) {
            winners.get(0).addBonusPoints(5);
            System.out.println(winners.get(0).getName() + " gets +5 suit bonus");
        } else {
            winners.forEach(p -> p.addBonusPoints(2));
            System.out.println("Suit count tie - each gets +2");
        }
    }

    private static int longestConsecutive(List<Card> collected) {
        if (collected.isEmpty()) return 0;
        Set<Integer> vals = collected.stream()
                .map(c -> c.getRank().getValue())
                .collect(Collectors.toSet());
        return vals.stream()
                .filter(v -> !vals.contains(v - 1))   // only sequence starts
                .mapToInt(v -> { int length = 1; while (vals.contains(v + length)) length++; return length; })
                .max().orElse(0);
    }
}
