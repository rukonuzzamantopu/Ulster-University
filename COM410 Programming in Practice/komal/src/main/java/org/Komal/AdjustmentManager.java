package org.Komal;


import java.util.*;
/**
 * Optional adjustment stage allowing players to discard and draw replacements.
 */

public class AdjustmentManager {
    private final List<Player> players;
    private final Deck deck;
    private final Scanner scanner;

    public AdjustmentManager(List<Player> players, Deck deck, Scanner scanner) {
        this.players = players;
        this.deck = deck;
        this.scanner = scanner;
    }

    public void execute() {
        System.out.println("*******************************");
        System.out.println("\n--- Optional Adjustment Stage ---");
        System.out.println("*******************************");
        for (Player p : players) {
            System.out.println("Player: " + p.GET_NAME());

            if (p.GET_COLLECTED().isEmpty()) {
                System.out.println(" No collected cards to adjust."); continue;
            }
            if (p.HAS_USED_ADJUSTMENT()) {
                System.out.println(" Already used adjustment."); continue;
            }

            int toDiscard = p.IS_COMPUTER() ? computerDiscardCount(p) : humanDiscardCount(p);

            if (toDiscard == 0) { System.out.println("No cards discarded."); continue; }

            List<Card> removed = pickCardsToDiscard(p, toDiscard);
            deck.RETURN_MANY_TO_BOTTOM(removed);

            for (int i = 0; i < toDiscard; i++) {
                Card drawn = deck.DRAW_TOP();
                if (drawn != null) p.ADD_COLLECTED(drawn);
            }
            p.SET_USED_ADJUSTMENT(true);
            System.out.println("After adjustment: " + formatCards(p.GET_COLLECTED()));
        }
    }

    private int computerDiscardCount(Player p) {
        int max  = Math.min(2, p.GET_COLLECTED().size());
        int pick = new Random().nextInt(max + 1);
        System.out.println(" Computer chooses to discard " + pick + " card(s).");
        return pick;
    }

    private int humanDiscardCount(Player p) {
        if (!promptYesNo("Do you want to replace cards (y/n)? ")) {
            System.out.println("No cards discarded.");
            return 0;
        }
        System.out.println("Your cards:");
        List<Card> col = p.GET_COLLECTED();
        for (int i = 0; i < col.size(); i++) System.out.println((i + 1) + ":" + col.get(i));
        int max = Math.min(2, col.size());
        return promptInt("How many cards would you like to discard? (0-" + max + "): ", 0, max);
    }

    private List<Card> pickCardsToDiscard(Player p, int toDiscard) {
        List<Card> removed = new ArrayList<>();
        if (p.IS_COMPUTER()) {
            Collections.shuffle(p.GET_COLLECTED());
            for (int i = 0; i < toDiscard; i++) removed.add(p.GET_COLLECTED().remove(0));
        } else {
            for (int i = 0; i < toDiscard; i++) {
                int idx = promptInt(
                        "Enter 1-based index of card to discard (remaining " + (toDiscard - i) + "): ",
                        1, p.GET_COLLECTED().size());
                removed.add(p.GET_COLLECTED().remove(idx - 1));
            }
        }
        return removed;
    }

    private int promptInt(String msg, int lo, int hi) {
        while (true) {
            System.out.print(msg);
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= lo && v <= hi) return v;
            } catch (NumberFormatException ignored) { }
        }
    }

    private boolean promptYesNo(String msg) {
        while (true) {
            System.out.print(msg);
            String line = scanner.nextLine().trim().toLowerCase();
            if (line.equals("y") || line.equals("yes")) return true;
            if (line.equals("n") || line.equals("no")) return false;
        }
    }

    private String formatCards(List<Card> list) {
        if (list.isEmpty()) return "(none)";
        StringBuilder sb = new StringBuilder();
        for (Card c : list) sb.append(c).append(" ");
        return sb.toString().trim();
    }
}
