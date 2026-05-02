package org.example;


import java.util.*;
import java.util.stream.Collectors;

/** Handles dealing and resolving rounds. */
public class RoundResolver {
    private final List<Player> players;
    private final Deck deck;
    private final ConsoleUI ui;

    public RoundResolver(List<Player> players, Deck deck, ConsoleUI ui) {
        this.players = players;
        this.deck = deck;
        this.ui = ui;
    }

    public void playRounds(int rounds) {
        for (int round = 1; round <= rounds; round++) {
            ui.println("\n--- Round " + round + " ---");
            Map<Player, Card> dealt = dealOneCardEach();
            resolveRound(dealt);
            printScores(round);
            ui.promptEnter();
        }
    }

    private Map<Player, Card> dealOneCardEach() {
        Map<Player, Card> dealt = new LinkedHashMap<>();
        for (Player player : players) {
            Card card = deck.drawTopCard();
            dealt.put(player, card);
            ui.println(player.getName() + " receives: " + (card != null ? card : "[no card]"));
        }
        return dealt;
    }

    private void resolveRound(Map<Player, Card> dealt) {
        int bestValue = dealt.values().stream()
                .filter(Objects::nonNull)
                .mapToInt(c -> c.getRank().getValue())
                .max().orElse(-1);

        List<Player> winners = dealt.entrySet().stream()
                .filter(e -> e.getValue() != null && e.getValue().getRank().getValue() == bestValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (winners.isEmpty()) {
            ui.println("No valid cards dealt this round.");
            return;
        }

        returnLosingCards(dealt, winners);

        if (winners.size() == 1) {
            Player winner = winners.get(0);
            winner.addCollected(dealt.get(winner));
            winner.addRoundPoints(3);
            ui.println("Winner: " + winner.getName() + " (+3) keeps " + dealt.get(winner));
        } else {
            ui.print("Tie between:");
            for (Player t : winners) {
                t.addCollected(dealt.get(t));
                t.addRoundPoints(1);
                ui.print(" " + t.getName());
            }
            ui.println(" (+1 each)");
        }
    }

    private void returnLosingCards(Map<Player, Card> dealt, List<Player> winners) {
        dealt.entrySet().stream()
                .filter(e -> !winners.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .forEach(deck::returnCardToBottom);
    }

    private void printScores(int round) {
        ui.println("Scores after round " + round + ":");
        players.forEach(player -> ui.println(player.getName() + ": " + player.getRoundPoints()));
    }
}
