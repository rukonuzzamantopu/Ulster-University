package org.example;


import java.util.*;

/**
 * Manages the optional adjustment stage where players may discard
 * and replace collected cards. The manager supports both human
 * and simple computer decision logic.
 */
public class AdjustmentManager {
    /**
     * Run the adjustment phase for every player in the given game state.
     * - Computer players discard a random number of cards (0..max)
     * - Human players are prompted for whether and how many cards to replace
     * The method returns discarded cards to the deck bottom and draws replacements.
     */
    public void AdjustmentStage(GameState state) {
        CollectionPrinter printer = new CollectionPrinter();
        System.out.println("\n###### Optional Adjustment Stage ######");

        for (Player P : state.Players) {
            System.out.println("Player: " + P.GetName());

            // Skip players who have nothing to adjust or already used the adjustment
            if (P.GetCollected().isEmpty()) { System.out.println(" No collected cards to adjust."); continue; }
            if (P.HasUsedAdjustment()) { System.out.println(" Already used adjustment."); continue; }

            int ToDiscard = 0;

            // Computer decision: choose a random number to discard (0..max)
            if (P.IsComputer()) {
                int Max = Math.min(2, P.GetCollected().size());
                ToDiscard = new Random().nextInt(Max + 1);
                System.out.println(" Computer chooses to discard " + ToDiscard + " card(s).");

                // Human decision: prompt whether to replace and how many
            } else {
                System.out.print("Do you want to replace cards? (Yes/no): ");
                String Ans = state.Scanner.nextLine().trim().toLowerCase();
                if (!Ans.isEmpty() && Ans.charAt(0) == 'y') {
                    int Max = Math.min(2, P.GetCollected().size());
                    if (Max == 0) {
                        System.out.println("You have no collected cards to replace.");
                        ToDiscard = 0;
                    } else {
                        int Temp = -1;
                        // Read a valid integer between 1 and Max
                        do {
                            System.out.print("How many cards do you want to replace? (1-" + Max + "): ");
                            String Line = state.Scanner.nextLine().trim();
                            try { Temp = Integer.parseInt(Line); } catch (Exception e) { Temp = -1; }
                        } while (Temp < 1 || Temp > Max);
                        ToDiscard = Temp;

                        // Show current collected cards so player can pick indices later
                        System.out.println("Your cards:");
                        List<Card> Ccol = P.GetCollected();
                        for (int I = 0; I < Ccol.size(); I++) System.out.println((I+1) + ":" + Ccol.get(I));
                    }
                } else {
                    System.out.println("No adjustment made.");
                    ToDiscard = 0;
                }
            }

            if (ToDiscard == 0) { System.out.println("No cards discarded."); continue; }

            // Build list of removed cards (either chosen randomly for computer
            // or selected by index for human players)
            List<Card> Removed = new ArrayList<>();
            if (P.IsComputer()) {
                List<Card> Ccol = P.GetCollected();
                Collections.shuffle(Ccol);
                for (int I=0;I<ToDiscard;I++) Removed.add(Ccol.remove(0));
            } else {
                System.out.println("Your cards: " + printer.FormatCards(P.GetCollected()));
                for (int I=0;I<ToDiscard;I++) {
                    int Idx = -1;
                    do {
                        System.out.print("Enter 1-based index of card to discard (remaining " + (ToDiscard - I) + "): ");
                        String Line = state.Scanner.nextLine().trim();
                        try { Idx = Integer.parseInt(Line); } catch (Exception e) { Idx = -1; }
                    } while (Idx < 1 || Idx > P.GetCollected().size());
                    Card Rem = P.GetCollected().remove(Idx-1);
                    Removed.add(Rem);
                }
            }

            // Return removed cards to deck bottom and draw replacements
            state.Deck.ReturnManyToBottom(Removed);
            for (int I=0;I<ToDiscard;I++) {
                Card Draw = state.Deck.DrawTop();
                if (Draw != null) P.AddCollected(Draw);
            }

            // Mark the player as having used their adjustment and show new hand
            P.SetUsedAdjustment(true);
            System.out.println("After adjustment: " + printer.FormatCards(P.GetCollected()));
        }
    }
}
