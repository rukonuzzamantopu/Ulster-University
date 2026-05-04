package org.Piyal;

import java.util.*;

public class ResultsManager {
    public void ShowFinalResults(GameState state) {
        System.out.println("\n=== Final Standings ===");
        state.Players.sort(Comparator.comparingInt(Player::GetFinalScore).reversed());
        int rank = 1;
        for (Player P : state.Players) {
            System.out.println(String.format("%d. %s | Total=%d (Round=%d, Bonus=%d)", rank++, P.GetName(), P.GetFinalScore(), P.GetRoundPoints(), P.GetBonusPoints()));
        }

        int Top = state.Players.get(0).GetFinalScore();
        List<Player> TopPlayers = new ArrayList<>();
        for (Player P : state.Players) if (P.GetFinalScore() == Top) TopPlayers.add(P);
        if (TopPlayers.size() == 1) System.out.println("\nWinner: " + TopPlayers.get(0).GetName() + " — final score: " + Top);
        else {
            System.out.print("\nDraw between: ");
            for (int i = 0; i < TopPlayers.size(); i++) {
                System.out.print(TopPlayers.get(i).GetName());
                if (i < TopPlayers.size() - 1) System.out.print(", ");
            }
            System.out.println(" — final score: " + Top);
        }
    }
}