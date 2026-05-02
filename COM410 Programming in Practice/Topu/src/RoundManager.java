

import java.util.*;

public class RoundManager {
    public void PlayRounds(GameState state) {
        for (int Round = 1; Round <= state.Rounds; Round++) {
            System.out.println("\n###### ##### ######");
            System.out.println("\nRound " + Round + " of " + state.Rounds);
             System.out.println("\n###### ##### ######");
            Map<Player, Card> Dealt = new LinkedHashMap<>();
            for (Player P : state.Players) {
                Card C = state.Deck.DrawTop();
                Dealt.put(P, C);
                System.out.println(P.GetName() + " receives: " + (C != null ? C : "[no card]"));
            }

            int Best = -1;
            for (Card C : Dealt.values()) if (C != null) Best = Math.max(Best, C.GetRank().GetValue());
            List<Player> Winners = new ArrayList<>();
            for (Map.Entry<Player, Card> E : Dealt.entrySet()) {
                Card C = E.getValue();
                if (C != null && C.GetRank().GetValue() == Best) Winners.add(E.getKey());
            }

            if (Winners.isEmpty()) {
                System.out.println("No valid cards dealt this round.");
            } else if (Winners.size() == 1) {
                Player Winner = Winners.get(0);
                Card Won = Dealt.get(Winner);
                Winner.AddCollected(Won);
                Winner.AddRoundPoints(3);
                System.out.println("Winner: " + Winner.GetName() + " (+3) keeps " + Won);
                for (Map.Entry<Player, Card> E : Dealt.entrySet()) {
                    if (!E.getKey().equals(Winner)) state.Deck.ReturnToBottom(E.getValue());
                }
            } else {
                System.out.print("Tie Between:");
                for (Player T : Winners) {
                    Card C = Dealt.get(T);
                    T.AddCollected(C);
                    T.AddRoundPoints(1);
                    System.out.print(" " + T.GetName());
                }
                System.out.println(" (+1 each)");
                for (Map.Entry<Player, Card> E : Dealt.entrySet()) {
                    if (!Winners.contains(E.getKey())) state.Deck.ReturnToBottom(E.getValue());
                }
            }

            System.out.println("After round Scores " + Round + ":");
            for (Player P : state.Players) System.out.println(P.GetName() + ": " + P.GetRoundPoints());

            System.out.print("Press Enter to continue...");
            state.Scanner.nextLine();
        }
    }
}
