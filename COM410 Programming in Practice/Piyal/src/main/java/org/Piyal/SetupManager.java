package org.Piyal;

import java.util.Scanner;

public class SetupManager {
    public void SetupPlayers(GameState state) {
        int N = 0;
        do {
            System.out.print("  How many players will join? (2 to 5): ");
            String Line = state.Scanner.nextLine().trim();
            try { N = Integer.parseInt(Line); } catch (Exception e) { N = 0; }
        } while (N < 2 || N > 5);

        for (int I = 1; I <= N; I++) {
            System.out.print("Enter name for player " + I + " (or unique): ");
            String Name = state.Scanner.nextLine().trim();
            if (Name.isEmpty()) Name = "Player" + I;
            state.Players.add(new Player(Name));
        }
    }

    public void SetupRounds(GameState state) {
        int R = 0;
        do {
            System.out.print("Enter number of rounds (5-10): ");
            String Line = state.Scanner.nextLine().trim();
            try { R = Integer.parseInt(Line); } catch (Exception e) { R = 0; }
        } while (R < 5 || R > 10);
        state.Rounds = R;
    }
}

