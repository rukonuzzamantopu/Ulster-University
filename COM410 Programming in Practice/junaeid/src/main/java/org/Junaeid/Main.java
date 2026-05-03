package org.Junaeid;

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
        System.out.println("==================================================");
        System.out.println("        HIGH CARD SERIES  --  Console Game       ");
        System.out.println("==================================================");
        System.out.println("  How are you??? I am Junaeid!");
        System.out.println("  Welcome to my card game. Let us have fun!");
        System.out.println("==================================================\n");
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
            System.out.println("  Nice! Welcome to the game, " + playerName + "!");
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

            System.out.println("\n========== Round " + currentRound + " of " + totalRounds + " ==========");
            System.out.println("  Junaeid is dealing the cards...");

            Map<Player, Card> dealtCardsMap = new LinkedHashMap<>();

            // Deal cards to each player
            for (Player currentPlayer : players) {
                Card drawnCard = deck.drawTop();
                dealtCardsMap.put(currentPlayer, drawnCard);

                System.out.println("  -> " + currentPlayer.getName() + " receives: " + (drawnCard != null ? drawnCard : "[no card]"));
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

                System.out.println();
                System.out.println("  *** Round winner: " + winner.getName() + " (+3 points) -- keeps " + winningCard + " ***\n");

                for (Map.Entry<Player, Card> entry : dealtCardsMap.entrySet()) {
                    if (!entry.getKey().equals(winner)) {
                        deck.returnToBottom(entry.getValue());
                    }
                }
            }
            else {
                System.out.print("  Tie between:");

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
            System.out.println("\n  -- Scoreboard after round " + currentRound + " --");

            for (Player player : players) {
                System.out.println("     " + player.getName() + ": " + player.getRoundPoints() + " pts");
            }

            System.out.print("\nPress Enter to go to the next round...");
            scanner.nextLine();
        }
    }

    private void displayPlayerCollections() {
        System.out.println("\n==================================================");
        System.out.println("        Collections and Round Scores            ");
        System.out.println("==================================================\n");

        for (Player player : players) {
            System.out.println("  Player : " + player.getName());
            System.out.println("  Points : " + player.getRoundPoints());
            System.out.println("  Cards  :");

            List<Card> collectedCards = player.getCollected();

            if (collectedCards.isEmpty()) {
                System.out.println("    (none)");
            } else {
                for (int index = 0; index < collectedCards.size(); index++) {
                    System.out.println("    " + (index + 1) + ". " + collectedCards.get(index));
                }
            }

            System.out.println();
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
        System.out.println("\n==================================================");
        System.out.println("          Optional Adjustment Stage             ");
        System.out.println("==================================================\n");

        System.out.println("  Junaeid allows each player one chance to swap cards!\n");

        for (Player player : players) {

            System.out.println("  -- " + player.getName() + " --");

            List<Card> collectedCards = player.getCollected();

            System.out.println("  Your current cards:");

            if (collectedCards.isEmpty()) {
                System.out.println("(none)");
                continue;
            }

            for (int index = 0; index < collectedCards.size(); index++) {
                System.out.println("    " + (index + 1) + ". " + collectedCards.get(index));
            }

            if (player.hasUsedAdjustment()) {
                System.out.println("  Adjustment already used.");
                continue;
            }

            int discardCount = 0;

            if (player.isComputer()) {

                int maxDiscard = Math.min(2, collectedCards.size());
                discardCount = new Random().nextInt(maxDiscard + 1);

                System.out.println("Computer discards " + discardCount + " card(s).");
            } else {
                System.out.print("Do you want to replace cards? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();

                if (!response.isEmpty() && response.charAt(0) == 'y') {

                    int maxDiscard = Math.min(2, collectedCards.size());

                    do {
                        System.out.print("How many cards to replace? (1-" + maxDiscard + "): ");
                        String input = scanner.nextLine().trim();

                        try {
                            discardCount = Integer.parseInt(input);
                        } catch (Exception e) {
                            discardCount = -1;
                        }

                    } while (discardCount < 1 || discardCount > maxDiscard);

                } else {
                    System.out.println("  Good call -- keeping your hand as it is!");
                }
            }

            if (discardCount == 0) continue;

            List<Card> removedCards = new ArrayList<>();

            if (player.isComputer()) {

                Collections.shuffle(collectedCards);

                for (int i = 0; i < discardCount; i++) {
                    removedCards.add(collectedCards.remove(0));
                }

            } else {

                for (int i = 0; i < discardCount; i++) {

                    int selectedIndex;
                    int remaining = discardCount - i;

                    do {
                        System.out.print("Pick card number to discard (" + remaining + " left): ");
                        String input = scanner.nextLine().trim();

                        try {
                            selectedIndex = Integer.parseInt(input);
                        } catch (Exception e) {
                            selectedIndex = -1;
                        }

                    } while (selectedIndex < 1 || selectedIndex > player.getCollected().size());

                    removedCards.add(player.getCollected().remove(selectedIndex - 1));
                }
            }

            deck.returnManyToBottom(removedCards);

            for (int i = 0; i < discardCount; i++) {
                Card newCard = deck.drawTop();
                if (newCard != null) player.addCollected(newCard);
            }

            player.setUsedAdjustment(true);

            System.out.println("  Hand after adjustment: " + formatCardList(player.getCollected()) + "\n");
        }
    }

    private void computeBonuses() {
        System.out.println("\n==================================================");
        System.out.println("              Calculating Bonuses                ");
        System.out.println("==================================================\n");

        System.out.println("  Junaeid is checking your card collections...\n");

        int bestSequenceLength = 0;
        Map<Player, Integer> sequenceMap = new HashMap<>();

        for (Player player : players) {
            int length = findLongestSequence(player.getCollected());

            sequenceMap.put(player, length);
            bestSequenceLength = Math.max(bestSequenceLength, length);

            System.out.println("  " + player.getName() + " -> longest consecutive run: " + length);
        }

        // Decide threshold for awarding bonuses (require at least length 3)
        final int BONUS_THRESHOLD = 3;

        if (bestSequenceLength < BONUS_THRESHOLD) {
            System.out.println("\n  No sequence bonuses awarded (no sequence length >= " + BONUS_THRESHOLD + ").");
        } else {

            // Find players who achieved the best sequence length
            List<Player> bonusWinners = new ArrayList<>();

            for (Map.Entry<Player, Integer> entry : sequenceMap.entrySet()) {
                if (entry.getValue() == bestSequenceLength) {
                    bonusWinners.add(entry.getKey());
                }
            }

            // Award bonus points equal to the sequence length to each winner
            for (Player winner : bonusWinners) {
                winner.addBonusPoints(2); // smaller bonus for this variant
            }

            if (bonusWinners.size() > 1) {
                System.out.print("\n  ** Sequence bonus tied between:");
                for (Player p : bonusWinners) System.out.print(" " + p.getName());
                System.out.println("  (+2 each) **\n");
            }
        }

        // Additionally, simple suit-count bonus: give +5 to player with most same-suit cards
        Map<Player, Integer> suitBest = new HashMap<>();

        for (Player player : players) {
            Map<String, Integer> suitCount = new HashMap<>();
            for (Card c : player.getCollected()) {
                suitCount.merge(c.getSuit().toString(), 1, Integer::sum);
            }

            int best = suitCount.values().stream().max(Integer::compareTo).orElse(0);
            suitBest.put(player, best);
        }

        // find top suit count
        int topSuit = suitBest.values().stream().max(Integer::compareTo).orElse(0);
        List<Player> suitWinners = new ArrayList<>();
        for (Map.Entry<Player, Integer> e : suitBest.entrySet()) if (e.getValue() == topSuit) suitWinners.add(e.getKey());

        for (Player p : players) {
            System.out.println("\n  " + p.getName() + " -> best suit count: " + suitBest.getOrDefault(p, 0));
        }

        if (!suitWinners.isEmpty()) {
            if (suitWinners.size() == 1) {
                Player winner = suitWinners.get(0);
                winner.addBonusPoints(5);
                System.out.println("\n  *** " + winner.getName() + " wins the Suit bonus! +5 pts ***\n");
            } else {
                System.out.print("\n  ** Suit bonus tied between:");

                for (Player p : suitWinners) {
                    p.addBonusPoints(2);
                    System.out.print(" " + p.getName());
                }

                System.out.println("  (+2 each) **\n");
            }
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
        System.out.println("\n==================================================");
        System.out.println("                 Final Results                   ");
        System.out.println("==================================================\n");

        players.sort(
                Comparator.comparingInt(Player::getFinalScore).reversed()
        );

        int rank = 1;
        for (Player player : players) {
            System.out.println(String.format("  #%d  %s   Round: %d  Bonus: %d  Total: %d", rank++, player.getName(), player.getRoundPoints(), player.getBonusPoints(), player.getFinalScore()));
        }

        // Determine winner(s)
        if (players.isEmpty()) {
            System.out.println("No players to determine a winner.");
            return;
        }

        int topScore = players.get(0).getFinalScore();
        List<Player> winners = new ArrayList<>();

        for (Player p : players) {
            if (p.getFinalScore() == topScore) winners.add(p);
            else break;
        }

        if (winners.size() == 1) {
            System.out.println("\nCongratulations " + winners.get(0).getName() + " you win this game and achieved " + topScore + " points");
        } else {
            System.out.print("\nTie between:");
            for (Player p : winners) System.out.print(" " + p.getName());
            System.out.println(" (" + topScore + " points each)");
        }
    }
}

