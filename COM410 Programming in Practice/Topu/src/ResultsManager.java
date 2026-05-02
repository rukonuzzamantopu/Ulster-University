

import java.util.*;

public class ResultsManager {
    public void ShowFinalResults(GameState state) {
        System.out.println("\n###### ##### ######");
        System.out.println("\nFinal Results ");
        System.out.println("\n###### ##### ######");
        state.Players.sort(Comparator.comparingInt(Player::GetFinalScore).reversed());
        for (Player P : state.Players) System.out.println(P.GetName() + " - Round: " + P.GetRoundPoints() + " Bonus: " + P.GetBonusPoints() + " Total: " + P.GetFinalScore());
        int Top = state.Players.get(0).GetFinalScore();
        List<Player> TopPlayers = new ArrayList<>();
        for (Player P : state.Players) if (P.GetFinalScore() == Top) TopPlayers.add(P);
        if (TopPlayers.size() == 1) System.out.println("Winner: " + TopPlayers.get(0).GetName());
        else {
            System.out.print("Draw between:");
            for (Player P : TopPlayers) System.out.print(" " + P.GetName());
            System.out.println();
        }
    }
}
