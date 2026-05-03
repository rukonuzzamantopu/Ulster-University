package org.example;

import java.util.*;

public class SwapManager {
    private final GameEngine engine;

    public SwapManager(GameEngine engine) {
        this.engine = engine;
    }

    public void conductSwapStage() {
        engine.emit("[ Optional Card Swap Stage ]");
        engine.emit("  " + engine.AUTHOR + " gives each player ONE chance to swap cards.");
        engine.emit("  .................................................");
        engine.emit("");

        List<Participant> roster = engine.getRoster();
        int idx = 0;
        if (roster.size() > 0) {
            do {
                Participant p = roster.get(idx);
            engine.emit("  > " + p.tag() + "'s turn");

            if (!p.hasWonCards()) {                        // Participant ADT operation
                engine.emit("    No cards to swap. Moving on.");
                engine.emit(""); idx++; continue;
            }
            if (p.swapOpportunityTaken()) {                // Participant ADT operation
                engine.emit("    Already used swap opportunity. Skipping.");
                engine.emit(""); idx++; continue;
            }

            int swapCount = p.botControlled()
                    ? decideAiSwap(p)
                    : decideHumanSwap(p);

            if (swapCount == 0) {
                engine.emit("    No cards swapped.");
                engine.emit(""); idx++; continue;
            }

            List<PlayingCard> thrown = collectCardsToSwap(p, swapCount);
            engine.getPile().appendMany(thrown);                       // CardPile ADT operation

            int drawn = 0;
            if (swapCount > 0) {
                do {
                    PlayingCard fresh = engine.getPile().pullTopCard();    // CardPile ADT operation
                    if (fresh != null) {
                        p.winCard(fresh);                      // Participant ADT operation
                        engine.emit("    Received : " + fresh.asText());
                    }
                    drawn++;
                } while (drawn < swapCount);
            }
            p.recordSwapOpportunityUsed();                 // Participant ADT operation
            engine.emit("    Hand after swap : " + engine.handAsString(p.peekWonPile()));
            engine.emit("");
            idx++;
            } while (idx < roster.size());
        }
    }

    private int decideAiSwap(Participant p) {
        int cap  = Math.min(2, p.wonPileSize());
        int pick = new Random().nextInt(cap + 1);
        engine.emit("    AI decides to swap " + pick + " card(s).");
        return pick;
    }

    private int decideHumanSwap(Participant p) {
        engine.emit("    Your current hand :");
        int ci = 0;
        if (p.wonPileSize() > 0) {
            do {
                engine.emit("      [" + (ci + 1) + "]  " + p.peekWonPile().get(ci).asText());
                ci++;
            } while (ci < p.wonPileSize());
        }
        engine.emitInline("    Do you want to swap any cards? (yes / no): ");
        String reply = engine.getScanner().nextLine().trim().toLowerCase();
        if (reply.isEmpty() || reply.charAt(0) != 'y') {
            engine.emit("    Holding current cards. Wise choice!");
            return 0;
        }
        int cap = Math.min(2, p.wonPileSize());
        if (cap == 0) { engine.emit("    No cards available to swap."); return 0; }
        int n = engine.grabNumber("    How many cards to swap? (1 to " + cap + "): ", 1, cap);
        return n;
    }

    private List<PlayingCard> collectCardsToSwap(Participant p, int swapCount) {
        List<PlayingCard> thrown = new ArrayList<>();
        if (p.botControlled()) {
            Collections.shuffle(p.peekWonPile());
            int ci = 0;
            if (swapCount > 0) {
                do {
                    thrown.add(p.extractCardAt(0));            // Participant ADT operation
                    ci++;
                } while (ci < swapCount);
            }
        } else {
            engine.emit("    Hand : " + engine.handAsString(p.peekWonPile()));
            int ci = 0;
            if (swapCount > 0) {
                do {
                    int pos = engine.grabNumber(
                            "    Select card number to discard (" + (swapCount - ci) + " remaining): ",
                            1, p.wonPileSize());
                    PlayingCard tossed = p.extractCardAt(pos - 1); // Participant ADT operation
                    thrown.add(tossed);
                    engine.emit("    Tossed : " + tossed.asText());
                    ci++;
                } while (ci < swapCount);
            }
        }
        return thrown;
    }

    
}
