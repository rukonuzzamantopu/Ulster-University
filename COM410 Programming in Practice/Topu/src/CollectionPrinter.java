

import java.util.List;

public class CollectionPrinter {
    public void ShowCollectionsAndScores(GameState state) {
        System.out.println("\n###### Collections and Round Scores ######");
        for (Player P : state.Players) {
            System.out.println(P.GetName() + " - Round points: " + P.GetRoundPoints());
            System.out.println("Collected:");
            List<Card> Col = P.GetCollected();
            if (Col.isEmpty()) {
                System.out.println("(none)");
            } else {
                for (int I = 0; I < Col.size(); I++) {
                    System.out.println((I + 1) + ": " + Col.get(I));
                }
            }
        }
    }

    public String FormatCards(List<Card> ListCards) {
        if (ListCards.isEmpty()) return "(none)";
        StringBuilder Sb = new StringBuilder();
        for (Card C : ListCards) Sb.append(C).append(" ");
        return Sb.toString().trim();
    }
}
