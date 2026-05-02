package org.example;

import java.util.*;

public class Main {
    private final List<Player> players = new ArrayList<>();
    private final Deck deck = new Deck();
    private final Scanner scanner = new Scanner(System.in);
    private int rounds = 5;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("High Card Series - Console Game");
        setupPlayers();
        setupRounds();
        deck.shuffle();
        playRounds();
        showCollectionsAndScores();
        adjustmentStage();
        applyBonuses();
        showFinalResults();
    }

    private void setupPlayers() {
        int n = 0;
        while (n < 2 || n > 5) {
            System.out.print("Enter number of players (2-5): ");
            String line = scanner.nextLine().trim();
            try { n = Integer.parseInt(line); } catch (Exception e) { n = 0; }
        }
        for (int i = 1; i <= n; i++) {
            System.out.print("Enter name for player  " + i + ":" );
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "Player" + i;
            players.add(new Player(name));
        }
    }

    private void setupRounds() {
        int r = 0;
        while (r < 5 || r > 10) {
            System.out.print("Enter number of rounds to play (5-10): ");
            String line = scanner.nextLine().trim();
            try { r = Integer.parseInt(line); } catch (Exception e) { r = 0; }
        }
        rounds = r;
    }

    private void playRounds() {
        for (int round = 1; round <= rounds; round++) {
            System.out.println("\n--- Round " + round + " ---");
            Map<Player, Card> dealt = new LinkedHashMap<>();
            for (Player p : players) {
                Card c = deck.drawTop();
                dealt.put(p, c);
                System.out.println(p.getName() + " receives: " + (c != null ? c : "[no card]"));
            }

            int best = -1;
            for (Card c : dealt.values()) if (c != null) best = Math.max(best, c.getRank().getValue());
            List<Player> winners = new ArrayList<>();
            for (Map.Entry<Player, Card> e : dealt.entrySet()) {
                Card c = e.getValue();
                if (c != null && c.getRank().getValue() == best) winners.add(e.getKey());
            }

            if (winners.isEmpty()) {
                System.out.println("No valid cards dealt this round.");
            } else if (winners.size() == 1) {
                Player winner = winners.get(0);
                Card won = dealt.get(winner);
                winner.addCollected(won);
                winner.addRoundPoints(3);
                System.out.println("Winner: " + winner.getName() + " (+3) keeps " + won);
                // return non-winning cards to bottom
                for (Map.Entry<Player, Card> e : dealt.entrySet()) {
                    if (!e.getKey().equals(winner)) deck.returnToBottom(e.getValue());
                }
            } else {
                System.out.print("Tie between:");
                for (Player t : winners) {
                    Card c = dealt.get(t);
                    t.addCollected(c);
                    t.addRoundPoints(1);
                    System.out.print(" " + t.getName());
                }
                System.out.println(" (+1 each)");
                for (Map.Entry<Player, Card> e : dealt.entrySet()) {
                    if (!winners.contains(e.getKey())) deck.returnToBottom(e.getValue());
                }
            }

            // show running scores
            System.out.println("Scores after round " + round + ":");
            for (Player p : players) System.out.println(p.getName() + ": " + p.getRoundPoints());

            // wait for user to press Enter to continue to next round
            System.out.print("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    private void showCollectionsAndScores() {
        System.out.println("\n--- Collections and Round Scores ---");
        for (Player p : players) {
            System.out.println(p.getName() + " - Round points: " + p.getRoundPoints());
            System.out.println("Collected:");
            List<Card> col = p.getCollected();
            if (col.isEmpty()) {
                System.out.println("(none)");
            } else {
                for (int i = 0; i < col.size(); i++) {
                    System.out.println((i + 1) + ": " + col.get(i));
                }
            }
        }
    }

    private String formatCards(List<Card> list) {
        if (list.isEmpty()) return "(none)";
        StringBuilder sb = new StringBuilder();
        for (Card c : list) sb.append(c).append(" ");
        return sb.toString().trim();
    }

    private void adjustmentStage() {
        System.out.println("\n--- Optional Adjustment Stage ---");
        for (Player p : players) {
            System.out.println("Player: " + p.getName());
            if (p.getCollected().isEmpty()) { System.out.println(" No collected cards to adjust."); continue; }
            if (p.hasUsedAdjustment()) { System.out.println(" Already used adjustment."); continue; }

            int toDiscard = 0;
            if (p.isComputer()) {
                // simple AI: if has at least 2 cards, random 0-2, else 0-1
                int max = Math.min(2, p.getCollected().size());
                toDiscard = new Random().nextInt(max + 1);
                System.out.println(" Computer chooses to discard " + toDiscard + " card(s).");
            } else {
                // ask player whether they want to replace cards
                System.out.print("Do you want to replace cards? (Yes/no): ");
                String ans = scanner.nextLine().trim().toLowerCase();
                if (!ans.isEmpty() && ans.charAt(0) == 'y') {
                    int max = Math.min(2, p.getCollected().size());
                    if (max == 0) {
                        System.out.println("You have no collected cards to replace.");
                        toDiscard = 0;
                    } else {
                        // ask how many to replace (1..max)
                        while (true) {
                            System.out.print("How many cards do you want to replace? (1-" + max + "): ");
                            String line = scanner.nextLine().trim();
                            try { toDiscard = Integer.parseInt(line); } catch (Exception e) { toDiscard = -1; }
                            if (toDiscard >=1 && toDiscard <= max) break;
                        }
                        // show current cards before selecting which to discard
                        System.out.println("Your cards:");
                        List<Card> ccol = p.getCollected();
                        for (int i = 0; i < ccol.size(); i++) System.out.println((i+1) + ":" + ccol.get(i));
                    }
                } else {
                    System.out.println("No adjustment made.");
                    toDiscard = 0;
                }
            }

            if (toDiscard == 0) { System.out.println("No cards discarded."); continue; }

            List<Card> removed = new ArrayList<>();
            if (p.isComputer()) {
                // remove random cards
                List<Card> ccol = p.getCollected();
                Collections.shuffle(ccol);
                for (int i=0;i<toDiscard;i++) removed.add(ccol.remove(0));
            } else {
                System.out.println("Your cards: " + formatCards(p.getCollected()));
                for (int i=0;i<toDiscard;i++) {
                    int idx = -1;
                    while (idx < 1 || idx > p.getCollected().size()) {
                        System.out.print("Enter 1-based index of card to discard (remaining " + (toDiscard - i) + "): ");
                        String line = scanner.nextLine().trim();
                        try { idx = Integer.parseInt(line); } catch (Exception e) { idx = -1; }
                    }
                    // 1-based index
                    Card rem = p.getCollected().remove(idx-1);
                    removed.add(rem);
                }
            }

            // return discarded to bottom then draw replacements from top
            deck.returnManyToBottom(removed);
            for (int i=0;i<toDiscard;i++) {
                Card draw = deck.drawTop();
                if (draw != null) p.addCollected(draw);
            }
            p.setUsedAdjustment(true);
            System.out.println("After adjustment: " + formatCards(p.getCollected()));
        }
    }

    private void applyBonuses() {
        System.out.println("\n--- Calculating Bonuses ---");
        // Longest consecutive sequence
        int bestSeq = 0;
        Map<Player, Integer> seqLen = new HashMap<>();
        for (Player p : players) {
            int len = longestConsecutive(p.getCollected());
            seqLen.put(p, len);
            bestSeq = Math.max(bestSeq, len);
            System.out.println(p.getName() + " longest consecutive length: " + len);
        }
        List<Player> seqWinners = new ArrayList<>();
        for (Map.Entry<Player,Integer> e : seqLen.entrySet()) if (e.getValue() == bestSeq && bestSeq>0) seqWinners.add(e.getKey());
        if (!seqWinners.isEmpty()) {
            if (seqWinners.size() == 1) { seqWinners.get(0).addBonusPoints(5); System.out.println(seqWinners.get(0).getName() + " gets +5 sequence bonus"); }
            else { for (Player p : seqWinners) { p.addBonusPoints(2); } System.out.println("Sequence tie - each gets +2"); }
        }

        // Highest suit count
        int bestSuit = 0;
        Map<Player, Integer> suitBest = new HashMap<>();
        for (Player p : players) {
            Map<Card.Suit,Integer> counts = new EnumMap<>(Card.Suit.class);
            for (Card.Suit s: Card.Suit.values()) counts.put(s, 0);
            for (Card c : p.getCollected()) counts.put(c.getSuit(), counts.get(c.getSuit())+1);
            int m = counts.values().stream().mapToInt(Integer::intValue).max().orElse(0);
            suitBest.put(p, m);
            bestSuit = Math.max(bestSuit, m);
            System.out.println(p.getName() + " best suit count: " + m);
        }
        List<Player> suitWinners = new ArrayList<>();
        for (Map.Entry<Player,Integer> e : suitBest.entrySet()) if (e.getValue() == bestSuit && bestSuit>0) suitWinners.add(e.getKey());
        if (!suitWinners.isEmpty()) {
            if (suitWinners.size() == 1) { suitWinners.get(0).addBonusPoints(5); System.out.println(suitWinners.get(0).getName() + " gets +5 suit bonus"); }
            else { for (Player p : suitWinners) p.addBonusPoints(2); System.out.println("Suit count tie - each gets +2"); }
        }
    }

    private int longestConsecutive(List<Card> collected) {
        if (collected.isEmpty()) return 0;
        Set<Integer> vals = new HashSet<>();
        for (Card c : collected) vals.add(c.getRank().getValue());
        List<Integer> list = new ArrayList<>(vals);
        Collections.sort(list);
        int best = 1, cur = 1;
        for (int i=1;i<list.size();i++) {
            if (list.get(i) == list.get(i-1)+1) { cur++; best = Math.max(best, cur); }
            else cur = 1;
        }
        return best;
    }

    private void showFinalResults() {
        System.out.println("\n--- Final Results ---");
        players.sort(Comparator.comparingInt(Player::getFinalScore).reversed());
        for (Player p : players) System.out.println(p.getName() + " - Round: " + p.getRoundPoints() + " Bonus: " + p.getBonusPoints() + " Total: " + p.getFinalScore());
        int top = players.get(0).getFinalScore();
        List<Player> topPlayers = new ArrayList<>();
        for (Player p : players) if (p.getFinalScore() == top) topPlayers.add(p);
        if (topPlayers.size() == 1) System.out.println("Winner: " + topPlayers.get(0).getName());
        else {
            System.out.print("Draw between:");
            for (Player p : topPlayers) System.out.print(" " + p.getName());
            System.out.println();
        }
    }
}
