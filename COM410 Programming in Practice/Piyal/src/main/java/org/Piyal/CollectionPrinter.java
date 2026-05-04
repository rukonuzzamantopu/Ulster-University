package org.Piyal;
import java.util.List;

public class CollectionPrinter {
    public void ShowCollectionsAndScores(GameState state) {
        System.out.println("\n=== Post-Round Summary ===");
        System.out.println("\nPlayer collections and round points:\n");
        for (Player P : state.Players) {
            System.out.println("Player: " + P.GetName());
            System.out.println("Round points: " + P.GetRoundPoints());
            List<Card> Col = P.GetCollected();
            System.out.println("Collected cards (" + Col.size() + "):");
            if (Col.isEmpty()) {
                System.out.println("  (none)");
            } else {
                for (int I = 0; I < Col.size(); I++) {
                    System.out.println("  " + (I + 1) + ") " + Col.get(I));
                }
            }
            System.out.println();
        }
    }

    public String FormatCards(List<Card> ListCards) {
        if (ListCards.isEmpty()) return "(none)";
        StringBuilder Sb = new StringBuilder();
        for (Card C : ListCards) Sb.append(C).append(" ");
        return Sb.toString().trim();
    }
}


