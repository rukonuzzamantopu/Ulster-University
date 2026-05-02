package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Core game engine: sets up players and runs the game flow. */
public class GameEngine {
    private final List<Player> players = new ArrayList<>();
    private final Deck deck = new Deck();
    private final ConsoleUI ui = new ConsoleUI();
    private final RoundResolver roundResolver = new RoundResolver(players, deck, ui);
    private int rounds = 5;

    public void run() {
        ui.println("\n--- ----------------------------- ---");
        ui.println("----High Card Series ---");
        ui.println("\n--- ----------------------------- ---");
        setupPlayers();
        setupRounds();
        deck.shuffleDeck();
        roundResolver.playRounds(rounds);
        showCollectionsAndScores();
        adjustmentStage();
        BonusCalculator.applyBonuses(players);
        showFinalResults();
    }

    private void setupPlayers() {
        int numPlayers = ui.readIntInRange("Enter number of players Min 2 Max 5: ", 2, 5);
        for (int i = 1; i <= numPlayers; i++) {
            ui.print("Enter name for player  " + i + ":");
            String name = ui.readLine();
            if (name.isEmpty()) name = "Player" + i;
            players.add(new Player(name));
        }
    }

    private void setupRounds() {
        rounds = ui.readIntInRange("Enter number of rounds to play Min 5 Max 10: ", 5, 10);
    }

    private void showCollectionsAndScores() {
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- Collections and Round Scores ---");
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- ----------------------------- ---");
        for (Player player : players) {
            ui.println(player.getName() + " - Round points: " + player.getRoundPoints());
            ui.println("Collected:");
            List<Card> collected = player.getCollected();
            if (collected.isEmpty()) {
                ui.println("(none)");
            } else {
                for (int i = 0; i < collected.size(); i++) {
                    ui.println((i + 1) + ": " + collected.get(i));
                }
            }
        }
    }

    private void adjustmentStage() {
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- Optional Adjustment Stage ---");
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- ----------------------------- ---");
        for (Player player : players) {
            ui.println("Player: " + player.getName());
            if (player.getCollected().isEmpty()) { ui.println(" No collected cards to adjust."); continue; }
            if (player.hasUsedAdjustment())       { ui.println(" Already used adjustment.");     continue; }

            int toDiscard = player.isComputer() ? decideComputerDiscard(player) : decideHumanDiscard(player);

            if (toDiscard == 0) { ui.println("No cards discarded."); continue; }

            List<Card> removed = removeCards(player, toDiscard);
            deck.returnCardsToBottom(removed);

            for (int i = 0; i < toDiscard; i++) {
                Card draw = deck.drawTopCard();
                if (draw != null) player.addCollected(draw);
            }
            player.setUsedAdjustment(true);
            ui.println("After adjustment: " + ui.formatCards(player.getCollected()));
        }
    }

    private int decideComputerDiscard(Player player) {
        int max = Math.min(2, player.getCollected().size());
        int n = new java.util.Random().nextInt(max + 1);
        ui.println(" Computer chooses to discard " + n + " card(s).");
        return n;
    }

    private int decideHumanDiscard(Player player) {
        ui.println("Your cards:");
        List<Card> collected = player.getCollected();
        for (int i = 0; i < collected.size(); i++) ui.println((i + 1) + ": " + collected.get(i));

        boolean wants = ui.readYes("Do you want to replace cards? (Yes/no): ");
        if (!wants) {
            ui.println("No adjustment made.");
            return 0;
        }
        int max = Math.min(2, player.getCollected().size());
        if (max == 0) { ui.println("You have no collected cards to replace."); return 0; }

        int n = ui.readIntInRange("How many cards do you want to replace? (1-" + max + "): ", 1, max);
        return n;
    }

    private List<Card> removeCards(Player player, int toDiscard) {
        List<Card> removed = new ArrayList<>();
        if (player.isComputer()) {
            Collections.shuffle(player.getCollected());
            for (int i = 0; i < toDiscard; i++) removed.add(player.getCollected().remove(0));
        } else {
            ui.println("Your cards: " + ui.formatCards(player.getCollected()));
            for (int i = 0; i < toDiscard; i++) {
                int index = ui.readIntInRange(
                        "Enter 1-based index of card to discard (remaining " + (toDiscard - i) + "): ",
                        1, player.getCollected().size());
                removed.add(player.getCollected().remove(index - 1));
            }
        }
        return removed;
    }

    // Sort players by final score in descending order (highest score first)
    private void showFinalResults() {
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- Final Results ---");
        ui.println("\n--- ----------------------------- ---");
        ui.println("\n--- ----------------------------- ---");
        players.sort((a, b) -> b.getFinalScore() - a.getFinalScore());
        players.forEach(player -> ui.println(
                player.getName() + " - Round: " + player.getRoundPoints()
                        + " Bonus: " + player.getBonusPoints()
                        + " Total: " + player.getFinalScore()));

        int topScore = players.get(0).getFinalScore();
        List<Player> topPlayers = players.stream()
                .filter(player -> player.getFinalScore() == topScore)
                .collect(java.util.stream.Collectors.toList());

        if (topPlayers.size() == 1) {
            ui.println("Winner: " + topPlayers.get(0).getName());
        } else {
            ui.print("Draw between:");
            topPlayers.forEach(player -> ui.print(" " + player.getName()));
            ui.println("");
        }
    }
}

