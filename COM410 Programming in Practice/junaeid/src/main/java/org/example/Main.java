package org.example;

import java.util.*;

public class Main {

    // List storing all players in the game
    private final List<Player> players = new ArrayList<>();

    // Deck of cards used during gameplay
    private final Deck deck = new Deck();

    // Scanner object for user input
    private final Scanner scanner = new Scanner(System.in);

    // Total number of rounds to be played
    private int totalRounds = 5;

    public static void main(String[] args) {
        new Main().startGame();
    }

    private void startGame() {
        System.out.println("High Card Series");
        setupPlayers();
        setupRounds();
        deck.shuffle();
        executeRounds();
        displayPlayerCollections();
        performAdjustmentPhase();
        computeBonuses();
        showFinalResults();
    }

    private void setupPlayers() {
        int playerCount;

        do {
            System.out.print("Enter number of players (2–5): ");
            String inputLine = scanner.nextLine().trim();
            playerCount = Utils.safeParseInt(inputLine, 0);
        } while (playerCount < 2 || playerCount > 5);

        for (int index = 1; index <= playerCount; index++) {
            System.out.print("Enter name for player " + index + ": ");
            String playerName = scanner.nextLine().trim();

            if (playerName.isEmpty()) {
                playerName = "Player" + index;
            }

            players.add(new Player(playerName));
        }
    }

    private void setupRounds() {
        int roundCount;

        do {
            System.out.print("Enter number of rounds (5–10): ");
            String inputLine = scanner.nextLine().trim();
            roundCount = Utils.safeParseInt(inputLine, 0);
        } while (roundCount < 5 || roundCount > 10);

        totalRounds = roundCount;
    }

    private void executeRounds() {
        for (int currentRound = 1; currentRound <= totalRounds; currentRound++) {

            System.out.println("\n-----------------------");
            System.out.println("Round " + currentRound);
            System.out.println("-----------------------");

            Map<Player, Card> dealtCardsMap = new LinkedHashMap<>();

            // Deal cards to each player
            for (Player currentPlayer : players) {
                Card drawnCard = deck.drawTop();
                dealtCardsMap.put(currentPlayer, drawnCard);

                System.out.println(currentPlayer.getName() +
                        " receives: " + (drawnCard != null ? drawnCard : "[no card]"));
            }

            // Find highest card value
            int highestCardValue = -1;
            for (Card card : dealtCardsMap.values()) {
                if (card != null) {
                    highestCardValue = Math.max(highestCardValue, card.getRank().getValue());
                }
            }

            // Determine winners
            List<Player> roundWinners = new ArrayList<>();

            for (Map.Entry<Player, Card> entry : dealtCardsMap.entrySet()) {
                Card card = entry.getValue();

                if (card != null && card.getRank().getValue() == highestCardValue) {
                    roundWinners.add(entry.getKey());
                }
            }

            // Handle results
            if (roundWinners.isEmpty()) {
                System.out.println("No valid cards dealt this round.");
            }
            else if (roundWinners.size() == 1) {

                Player winner = roundWinners.get(0);
                Card winningCard = dealtCardsMap.get(winner);

                winner.addCollected(winningCard);
                winner.addRoundPoints(3);

                System.out.println("Winner: " + winner.getName() +
                        " (+3) keeps " + winningCard);

                for (Map.Entry<Player, Card> entry : dealtCardsMap.entrySet()) {
                    if (!entry.getKey().equals(winner)) {
                        deck.returnToBottom(entry.getValue());
                    }
                }
            }
            else {
                System.out.print("Tie between:");

                for (Player tiedPlayer : roundWinners) {
                    Card card = dealtCardsMap.get(tiedPlayer);

                    tiedPlayer.addCollected(card);
                    tiedPlayer.addRoundPoints(1);

                    System.out.print(" " + tiedPlayer.getName());
                }

                System.out.println(" (+1 each)");

                for (Map.Entry<Player, Card> entry : dealtCardsMap.entrySet()) {
                    if (!roundWinners.contains(entry.getKey())) {
                        deck.returnToBottom(entry.getValue());
                    }
                }
            }

            // Display scores
            System.out.println("Scores after round " + currentRound + ":");

            for (Player player : players) {
                System.out.println(player.getName() + ": " + player.getRoundPoints());
            }

            System.out.print("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    private void displayPlayerCollections() {
        System.out.println("\n--- Collections and Round Scores ---");

        for (Player player : players) {
            System.out.println(player.getName() +
                    " - Round points: " + player.getRoundPoints());

            System.out.println("Collected cards:");

            List<Card> collectedCards = player.getCollected();

            if (collectedCards.isEmpty()) {
                System.out.println("(none)");
            } else {
                for (int index = 0; index < collectedCards.size(); index++) {
                    System.out.println((index + 1) + ": " + collectedCards.get(index));
                }
            }
        }
    }

    private String formatCardList(List<Card> cardList) {
        if (cardList.isEmpty()) return "(none)";

        StringBuilder result = new StringBuilder();

        for (Card card : cardList) {
            result.append(card).append(" ");
        }

        return result.toString().trim();
    }

    private void performAdjustmentPhase() {
        System.out.println("\n--- Optional Adjustment Stage ---");

        for (Player player : players) {

            System.out.println("Player: " + player.getName());

            if (player.getCollected().isEmpty()) {
                System.out.println("No collected cards to adjust.");
                continue;
            }

            if (player.hasUsedAdjustment()) {
                System.out.println("Adjustment already used.");
                continue;
            }

            int discardCount = 0;

            if (player.isComputer()) {

                int maxDiscard = Math.min(2, player.getCollected().size());
                discardCount = new Random().nextInt(maxDiscard + 1);

                System.out.println("Computer discards " + discardCount + " card(s).");
            }
            else {
                System.out.print("Do you want to replace cards? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();

                if (!response.isEmpty() && response.charAt(0) == 'y') {

                    int maxDiscard = Math.min(2, player.getCollected().size());

                    do {
                        System.out.print("How many cards to replace (1-" + maxDiscard + "): ");
                        String input = scanner.nextLine().trim();

                        try {
                            discardCount = Integer.parseInt(input);
                        } catch (Exception e) {
                            discardCount = -1;
                        }

                    } while (discardCount < 1 || discardCount > maxDiscard);

                } else {
                    System.out.println("No adjustment made.");
                }
            }

            if (discardCount == 0) continue;

            List<Card> removedCards = new ArrayList<>();

            if (player.isComputer()) {

                List<Card> collectedCards = player.getCollected();
                Collections.shuffle(collectedCards);

                for (int i = 0; i < discardCount; i++) {
                    removedCards.add(collectedCards.remove(0));
                }

            } else {

                for (int i = 0; i < discardCount; i++) {

                    int selectedIndex;

                    do {
                        System.out.print("Select card index to discard: ");
                        String input = scanner.nextLine().trim();

                        try {
                            selectedIndex = Integer.parseInt(input);
                        } catch (Exception e) {
                            selectedIndex = -1;
                        }

                    } while (selectedIndex < 1 ||
                            selectedIndex > player.getCollected().size());

                    removedCards.add(player.getCollected().remove(selectedIndex - 1));
                }
            }

            deck.returnManyToBottom(removedCards);

            for (int i = 0; i < discardCount; i++) {
                Card newCard = deck.drawTop();
                if (newCard != null) player.addCollected(newCard);
            }

            player.setUsedAdjustment(true);

            System.out.println("After adjustment: " +
                    formatCardList(player.getCollected()));
        }
    }

    private void computeBonuses() {

        System.out.println("\n--- Calculating Bonuses ---");

        int bestSequenceLength = 0;
        Map<Player, Integer> sequenceMap = new HashMap<>();

        for (Player player : players) {
            int length = findLongestSequence(player.getCollected());

            sequenceMap.put(player, length);
            bestSequenceLength = Math.max(bestSequenceLength, length);

            System.out.println(player.getName() +
                    " longest sequence: " + length);
        }
    }

    private int findLongestSequence(List<Card> collectedCards) {

        if (collectedCards.isEmpty()) return 0;

        Set<Integer> uniqueValues = new HashSet<>();

        for (Card card : collectedCards) {
            uniqueValues.add(card.getRank().getValue());
        }

        List<Integer> sortedValues = new ArrayList<>(uniqueValues);
        Collections.sort(sortedValues);

        int longest = 1;
        int current = 1;

        for (int i = 1; i < sortedValues.size(); i++) {

            if (sortedValues.get(i) == sortedValues.get(i - 1) + 1) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }

        return longest;
    }

    private void showFinalResults() {

        System.out.println("\n--- Final Results ---");

        players.sort(
                Comparator.comparingInt(Player::getFinalScore).reversed()
        );

        for (Player player : players) {
            System.out.println(player.getName() +
                    " | Round: " + player.getRoundPoints() +
                    " | Bonus: " + player.getBonusPoints() +
                    " | Total: " + player.getFinalScore());
        }
    }
}

