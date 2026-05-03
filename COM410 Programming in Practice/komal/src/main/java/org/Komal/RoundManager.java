package org.Komal;

import java.util.*;

/**
 * Runs game rounds: deal, determine winners, update scores.
 */
public class RoundManager {
    private final List<Player> players;
    private final Deck deck;
    private final Scanner scanner;
    private final int rounds;

    public RoundManager(List<Player> players, Deck deck, Scanner scanner, int rounds) {
        this.players = players;
        this.deck = deck;
        this.scanner = scanner;
        this.rounds = rounds;
    }

    public void execute() {
        for (int r = 1; r <= rounds; r++) {
            System.out.println("*******************************");
            System.out.println("\n--- Round  Number " + r + " ---");
            System.out.println("*******************************");

            Map<Player, Card> dealt = dealCards();
            processRound(dealt);
            printRoundScores(r);

            System.out.print("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    private Map<Player, Card> dealCards() {
        Map<Player, Card> dealt = new LinkedHashMap<>();
        for (Player p : players) {
            Card c = deck.DRAW_TOP();
            dealt.put(p, c);
            System.out.println(p.GET_NAME() + " receives: " + (c != null ? c : "[no card]"));
        }
        return dealt;
    }

    private void processRound(Map<Player, Card> dealt) {
        int highValue = highestValue(dealt);
        if (highValue < 0) { System.out.println("No valid cards dealt this round."); return; }

        List<Player> winners = playersWithValue(dealt, highValue);
        returnNonWinnerCards(dealt, winners);

        if (winners.size() == 1) {
            Player w = winners.get(0);
            w.ADD_COLLECTED(dealt.get(w));
            w.ADD_ROUND_POINTS(3);
            System.out.println("Winner: " + w.GET_NAME() + " (+3) keeps " + dealt.get(w));
        } else {
            System.out.print("Tie between:");
            for (Player t : winners) {
                t.ADD_COLLECTED(dealt.get(t));
                t.ADD_ROUND_POINTS(1);
                System.out.print(" " + t.GET_NAME());
            }
            System.out.println(" (+1 each)");
        }
    }

    private int highestValue(Map<Player, Card> dealt) {
        int best = -1;
        for (Card c : dealt.values()) {
            if (c != null && c.GET_RANK().GET_VALUE() > best) best = c.GET_RANK().GET_VALUE();
        }
        return best;
    }

    private List<Player> playersWithValue(Map<Player, Card> dealt, int value) {
        List<Player> result = new ArrayList<>();
        for (Map.Entry<Player, Card> e : dealt.entrySet()) {
            if (e.getValue() != null && e.getValue().GET_RANK().GET_VALUE() == value)
                result.add(e.getKey());
        }
        return result;
    }

    private void returnNonWinnerCards(Map<Player, Card> dealt, List<Player> winners) {
        for (Map.Entry<Player, Card> e : dealt.entrySet()) {
            if (!winners.contains(e.getKey())) deck.RETURN_TO_BOTTOM(e.getValue());
        }
    }

    private void printRoundScores(int round) {
        System.out.println("*******************************");
        System.out.println("After-round scores " + round + ":");
        for (Player p : players) System.out.println(p.GET_NAME() + ": " + p.GET_ROUND_POINTS());
    }
}
