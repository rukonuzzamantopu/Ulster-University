import java.util.*;

/**
 * ADT: Game (extracted from Main)
 * -------------------------------------------------
 * The Game coordinates all ADTs (Deck, Player, Card)
 * to run a full High Card Series session.
 */
public class Game {

    private static final String OWNER = "Hafeez";

    // Internal ADT instances (encapsulated)
    private final List<Player> players = new ArrayList<>();
    private final Deck         deck    = new Deck();
    private final Scanner      scanner = new Scanner(System.in);
    private int rounds = 5;

    public void run() {
        greetHafeez();
        setupPlayers();
        setupRounds();
        deck.shuffle();
        playRounds();
        showCollectionsAndScores();
        adjustmentStage();
        applyBonuses();
        showFinalResults();
        farewell();
    }

    // Greeting

    private void greetHafeez() {
        say("==================================================");
        say("        HIGH CARD SERIES  --  Console Game       ");
        say("==================================================");
        say("  How are you??? I am " + OWNER + "!");
        say("  Welcome to my card game. Let us have fun!");
        say("==================================================");
        say("");
    }

    private void farewell() {
        say("");
        say("--------------------------------------------------");
        say("  Thanks for playing " + OWNER + "'s High Card Series!");
        say("  Come back soon -- " + OWNER + " will be waiting. :)");
        say("--------------------------------------------------");
    }

    private void say(String msg) {
        System.out.println(msg);
    }

    private void setupPlayers() {
        int count = readInt("Enter number of players (2-5): ", 2, 5);
        int i = 1;
        while (i <= count) {
            System.out.print("Enter name for player " + i + ": ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "Player" + i;
            players.add(new Player(name));
            say("  Nice! Welcome to the game, " + name + "!");
            i++;
        }
        say("");
    }

    private void setupRounds() {
        rounds = readInt("Enter number of rounds to play (5-10): ", 5, 10);
        say("  Alright! " + OWNER + " has set up " + rounds + " rounds. Good luck!");
        say("");
    }

    private void playRounds() {
        int r = 1;
        while (r <= rounds) {
            say("\n========== Round " + r + " of " + rounds + " ==========");
            Map<Player, Card> dealt = dealCards();
            resolveRound(dealt);
            showRoundScores(r);
            System.out.print("\nPress Enter to go to the next round...");
            scanner.nextLine();
            r++;
        }
    }

    private Map<Player, Card> dealCards() {
        Map<Player, Card> dealt = new LinkedHashMap<>();
        say("  " + OWNER + " is dealing the cards...");
        int i = 0;
        while (i < players.size()) {
            Player p  = players.get(i);
            Card   c  = deck.dealCard();
            dealt.put(p, c);
            say("  -> " + p.getName() + " receives: "
                    + (c != null ? c : "[no card -- deck is empty!]") );
            i++;
        }
        return dealt;
    }

    private void resolveRound(Map<Player, Card> dealt) {
        int best = findHighestValue(dealt);
        if (best < 0) {
            say("  No valid cards dealt this round. Moving on!");
            return;
        }

        List<Player> winners = findWinners(dealt, best);
        sendLosingCardsToDeck(dealt, winners);

        if (winners.size() == 1) {
            Player w = winners.get(0);
            w.collectCard(dealt.get(w));
            w.addRoundPoints(3);
            say("\n  *** Round winner: " + w.getName()
                    + " (+3 points) -- keeps " + dealt.get(w) + " ***");
        } else {
            System.out.print("\n  ** Tie between:");
            int i = 0;
            while (i < winners.size()) {
                Player t = winners.get(i);
                t.collectCard(dealt.get(t));
                t.addRoundPoints(1);
                System.out.print(" " + t.getName());
                i++;
            }
            say("  (+1 each) **");
        }
    }

    private int findHighestValue(Map<Player, Card> dealt) {
        int best = -1;
        Iterator<Card> it = dealt.values().iterator();
        while (it.hasNext()) {
            Card c = it.next();
            if (c != null && c.getValue() > best)
                best = c.getValue();
        }
        return best;
    }

    private List<Player> findWinners(Map<Player, Card> dealt, int best) {
        List<Player> winners = new ArrayList<>();
        Iterator<Map.Entry<Player, Card>> it = dealt.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Player, Card> e = it.next();
            if (e.getValue() != null && e.getValue().getValue() == best)
                winners.add(e.getKey());
        }
        return winners;
    }

    private void sendLosingCardsToDeck(Map<Player, Card> dealt, List<Player> winners) {
        Iterator<Map.Entry<Player, Card>> it = dealt.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Player, Card> e = it.next();
            if (!winners.contains(e.getKey()))
                deck.returnCard(e.getValue());
        }
    }

    private void showRoundScores(int round) {
        say("\n  -- Scoreboard after round " + round + " --");
        int i = 0;
        while (i < players.size()) {
            Player p = players.get(i);
            say("     " + p.getName() + ": " + p.getRoundPoints() + " pts");
            i++;
        }
    }

    private void showCollectionsAndScores() {
        say("\n==================================================");
        say("        Collections and Round Scores            ");
        say("==================================================");
        int i = 0;
        while (i < players.size()) {
            Player p = players.get(i);
            say("\n  Player : " + p.getName());
            say("  Points : " + p.getRoundPoints());
            say("  Cards  :");
            if (!p.hasCards()) {
                say("    (none)");
            } else {
                int j = 0;
                while (j < p.handSize()) {
                    say("    " + (j + 1) + ". " + p.getHand().get(j));
                    j++;
                }
            }
            i++;
        }
    }

    private String formatHand(List<Card> hand) {
        if (hand.isEmpty()) return "(none)";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < hand.size()) {
            sb.append(hand.get(i)).append("  ");
            i++;
        }
        return sb.toString().trim();
    }

    private void adjustmentStage() {
        say("\n==================================================");
        say("          Optional Adjustment Stage             ");
        say("==================================================");
        say("  " + OWNER + " allows each player one chance to swap cards!\n");

        int i = 0;
        while (i < players.size()) {
            Player p = players.get(i);
            say("  -- " + p.getName() + " --");

            if (!p.hasCards()) {
                say("  No collected cards to adjust. Skipping.\n");
                i++; continue;
            }
            if (p.hasUsedAdjustment()) {
                say("  Already used adjustment. Skipping.\n");
                i++; continue;
            }

            int toDiscard = p.isComputer()
                    ? decideComputerDiscard(p)
                    : decideHumanDiscard(p);

            if (toDiscard == 0) {
                say("  No cards discarded.\n");
                i++; continue;
            }

            List<Card> discarded = pickDiscards(p, toDiscard);
            deck.returnCards(discarded);

            int d = 0;
            while (d < toDiscard) {
                Card drawn = deck.dealCard();
                if (drawn != null) {
                    p.collectCard(drawn);
                    say("  Drew new card: " + drawn);
                }
                d++;
            }
            p.markAdjustmentUsed();
            say("  Hand after adjustment: " + formatHand(p.getHand()) + "\n");
            i++;
        }
    }

    private int decideComputerDiscard(Player p) {
        int max  = Math.min(2, p.handSize());
        int pick = new Random().nextInt(max + 1);
        say("  Computer thinks carefully... and discards " + pick + " card(s).");
        return pick;
    }

    private int decideHumanDiscard(Player p) {
        System.out.print("  Do you want to replace cards? (yes/no): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        if (ans.isEmpty() || ans.charAt(0) != 'y') {
            say("  Good call -- keeping your hand as it is!"); return 0;
        }
        int max = Math.min(2, p.handSize());
        if (max == 0) { say("  No cards available to replace."); return 0; }

        int n = readInt("  How many cards to replace? (1-" + max + "): ", 1, max);
        say("  Your current cards:");
        int i = 0;
        while (i < p.handSize()) {
            say("    " + (i + 1) + ". " + p.getHand().get(i));
            i++;
        }
        return n;
    }

    private List<Card> pickDiscards(Player p, int count) {
        List<Card> removed = new ArrayList<>();
        if (p.isComputer()) {
            Collections.shuffle(p.getHand());
            int i = 0;
            while (i < count) {
                removed.add(p.removeCardAt(0));
                i++;
            }
        } else {
            say("  Cards: " + formatHand(p.getHand()));
            int i = 0;
            while (i < count) {
                int idx = readInt(
                        "  Pick card number to discard (" + (count - i) + " left): ",
                        1, p.handSize());
                Card discarded = p.removeCardAt(idx - 1);
                removed.add(discarded);
                say("  Discarded: " + discarded);
                i++;
            }
        }
        return removed;
    }

    private void applyBonuses() {
        say("\n==================================================");
        say("              Calculating Bonuses               ");
        say("==================================================");
        say("  " + OWNER + " is checking your card collections...\n");
        applySequenceBonus();
        applySuitBonus();
    }

    private void applySequenceBonus() {
        int best = 0;
        Map<Player, Integer> seqMap = new LinkedHashMap<>();
        int i = 0;
        while (i < players.size()) {
            Player p   = players.get(i);
            int    len = longestConsecutiveRun(p.getHand());
            seqMap.put(p, len);
            if (len > best) best = len;
            say("  " + p.getName() + " -> longest consecutive run: " + len);
            i++;
        }
        say("");
        awardBonus(seqMap, best, "Sequence");
    }

    private void applySuitBonus() {
        int best = 0;
        Map<Player, Integer> suitMap = new LinkedHashMap<>();
        int i = 0;
        while (i < players.size()) {
            Player p = players.get(i);
            int    m = highestSuitCount(p.getHand());
            suitMap.put(p, m);
            if (m > best) best = m;
            say("  " + p.getName() + " -> best suit count: " + m);
            i++;
        }
        say("");
        awardBonus(suitMap, best, "Suit");
    }

    private int longestConsecutiveRun(List<Card> hand) {
        if (hand.isEmpty()) return 0;
        boolean[] present = new boolean[15];
        int i = 0;
        while (i < hand.size()) {
            present[hand.get(i).getValue()] = true;
            i++;
        }
        int best = 0, cur = 0, v = 2;
        while (v <= 14) {
            cur  = present[v] ? cur + 1 : 0;
            if (cur > best) best = cur;
            v++;
        }
        return best;
    }

    private int highestSuitCount(List<Card> hand) {
        int[] tally = new int[Card.Suit.values().length];
        int i = 0;
        while (i < hand.size()) {
            tally[hand.get(i).getSuit().ordinal()]++;
            i++;
        }
        int max = 0, j = 0;
        while (j < tally.length) {
            if (tally[j] > max) max = tally[j];
            j++;
        }
        return max;
    }

    private void awardBonus(Map<Player, Integer> scoreMap, int best, String label) {
        if (best <= 0) return;
        List<Player> top = new ArrayList<>();
        Iterator<Map.Entry<Player, Integer>> it = scoreMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Player, Integer> e = it.next();
            if (e.getValue() == best) top.add(e.getKey());
        }
        if (top.size() == 1) {
            top.get(0).addBonusPoints(5);
            say("  *** " + top.get(0).getName() + " wins the " + label + " bonus! +5 pts ***");
        } else {
            int i = 0;
            while (i < top.size()) {
                top.get(i).addBonusPoints(2);
                i++;
            }
            System.out.print("  ** " + label + " bonus tied between:");
            int j = 0;
            while (j < top.size()) {
                System.out.print(" " + top.get(j).getName());
                j++;
            }
            say("  (+2 each) **");
        }
        say("");
    }

    private void showFinalResults() {
        say("==================================================");
        say("                 Final Results                  ");
        say("==================================================");
        say("  " + OWNER + " presents the final standings:\n");

        players.sort((a, b) -> b.getTotalScore() - a.getTotalScore());

        int rank = 1, i = 0;
        while (i < players.size()) {
            Player p = players.get(i);
            say("  #" + rank + "  " + p.getName()
                    + "   Round: "  + p.getRoundPoints()
                    + "  Bonus: "   + p.getBonusPoints()
                    + "  Total: "   + p.getTotalScore());
            rank++;
            i++;
        }

        say("");
        int topScore = players.get(0).getTotalScore();
        List<Player> champions = new ArrayList<>();
        int j = 0;
        while (j < players.size()) {
            if (players.get(j).getTotalScore() == topScore)
                champions.add(players.get(j));
            j++;
        }

        if (champions.size() == 1) {
            say("  >>> Congratulations " + champions.get(0).getName() + "! You are the Champion! <<<");
            say("  " + OWNER + " is really proud of you!");
        } else {
            System.out.print("  >>> It is a draw between:");
            int k = 0;
            while (k < champions.size()) {
                System.out.print(" " + champions.get(k).getName());
                k++;
            }
            say("! <<<");
            say("  " + OWNER + " says: well played by everyone!");
        }
        say("");
    }

    private int readInt(String prompt, int lo, int hi) {
        int value;
        do {
            System.out.print(prompt);
            value = tryParse(scanner.nextLine().trim(), lo - 1);
            if (value < lo || value > hi)
                say("  Hey! " + OWNER + " says: please type a number between " + lo + " and " + hi + ".");
        } while (value < lo || value > hi);
        return value;
    }

    private int tryParse(String s, int fallback) {
        try   { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return fallback; }
    }
}
