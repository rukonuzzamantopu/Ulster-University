package org.Piyal;

import java.util.*;

public class RoundManager {
    public void PlayRounds(GameState state) {
        // Prompt before first round
        System.out.print("\nPress Enter to begin Round 1...");
        state.Scanner.nextLine();

        for (int Round = 1; Round <= state.Rounds; Round++) {
            System.out.println("\n=== Round " + Round + " ===");

            Map<Player, Card> Dealt = new LinkedHashMap<>();
            for (Player P : state.Players) {
                Card C = state.Deck.DrawTop();
                Dealt.put(P, C);
            }

            int Best = -1;
            Card.Rank bestRank = null;
            for (Card C : Dealt.values()) if (C != null && C.GetRank().GetValue() > Best) { Best = C.GetRank().GetValue(); bestRank = C.GetRank(); }

            if (Best == -1) {
                System.out.println("Highest rank this round: (none)");
                System.out.println("Result: No valid cards dealt this round.");
                for (Card c : Dealt.values()) state.Deck.ReturnToBottom(c);
            } else {
                System.out.println("Highest rank this round: " + formatRankVerbose(bestRank));

                List<Player> Winners = new ArrayList<>();
                for (Map.Entry<Player, Card> E : Dealt.entrySet()) {
                    Card C = E.getValue();
                    if (C != null && C.GetRank().GetValue() == Best) Winners.add(E.getKey());
                }

                if (Winners.size() == 1) {
                    System.out.println("Outcome: Sole winner keeps their card and receives +3 points.");
                    Player Winner = Winners.get(0);
                    Card Won = Dealt.get(Winner);
                    Winner.AddCollected(Won);
                    Winner.AddRoundPoints(3);
                    for (Map.Entry<Player, Card> E : Dealt.entrySet()) {
                        if (!E.getKey().equals(Winner)) state.Deck.ReturnToBottom(E.getValue());
                    }
                } else if (Winners.size() > 1) {
                    System.out.println("Outcome: Tie for highest rank — each tied player keeps their card and receives +1 point.");
                    for (Player T : Winners) {
                        Card C = Dealt.get(T);
                        T.AddCollected(C);
                        T.AddRoundPoints(1);
                    }
                    for (Map.Entry<Player, Card> E : Dealt.entrySet()) {
                        if (!Winners.contains(E.getKey())) state.Deck.ReturnToBottom(E.getValue());
                    }
                }

                // Print individual results
                for (Map.Entry<Player, Card> E : Dealt.entrySet()) {
                    Player P = E.getKey(); Card C = E.getValue();
                    String tag = "LOSE (+0)";
                    if (Winners.contains(P)) {
                        tag = (Winners.size() == 1) ? "WIN (+3)" : "TIE (+1)";
                    }
                    System.out.println(P.GetName() + " drew " + (C != null ? C : "[no card]") + " — " + tag);
                }
            }

            // Display scores and deck size
            System.out.println("\nScores after round:");
            for (Player P : state.Players) System.out.println("  " + P.GetName() + " = " + P.GetRoundPoints());
            System.out.println("Deck size after round: " + state.Deck.Size());

            // Next round prompt or final prompt with optional commands
            if (Round < state.Rounds) {
                System.out.println();
                System.out.println("Next: Round " + (Round + 1) + " of " + state.Rounds + ".");
                System.out.print("Press Enter to play the next round: ");
                String cmd = state.Scanner.nextLine().trim().toLowerCase();
              
            } else {
                System.out.print("\nAll rounds completed.\nPress Enter to continue: ");
                String cmd = state.Scanner.nextLine().trim().toLowerCase();
                if (cmd.equals("cards")) { CollectionPrinter cp = new CollectionPrinter(); for (Player p : state.Players) System.out.println(p.GetName() + ": " + cp.FormatCards(p.GetCollected())); }
                else if (cmd.equals("scores")) for (Player p : state.Players) System.out.println(p.GetName() + ": " + p.GetRoundPoints());
                else if (cmd.equals("help")) System.out.println("Available commands: 'cards' — show collected cards; 'scores' — show current scores; Enter — continue.");
            }
        }
    }

    private String formatRankVerbose(Card.Rank r) {
        if (r == null) return "(none)";
        switch (r) {
            case JACK: return "Jack (J)";
            case QUEEN: return "Queen (Q)";
            case KING: return "King (K)";
            case ACE: return "Ace (A)";
            case TEN: return "Ten (10)";
            default: return r.GetValue() + " (" + r.GetValue() + ")";
        }
    }
}


