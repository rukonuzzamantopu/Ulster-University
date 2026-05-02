

import java.util.Scanner;

public class SetupManager {
    public void SetupPlayers(GameState state) {
        int N = 0;
        do {
            System.out.print("Number of players Between 2 and 5: ");
            String Line = state.Scanner.nextLine().trim();
            try { N = Integer.parseInt(Line); } catch (Exception e) { N = 0; }
        } while (N < 2 || N > 5);

        for (int I = 1; I <= N; I++) {
            System.out.print("Name for player  " + I + ":" );
            String Name = state.Scanner.nextLine().trim();
            if (Name.isEmpty()) Name = "Player" + I;
            state.Players.add(new Player(Name));
        }
    }

    public void SetupRounds(GameState state) {
        int R = 0;
        do {
            System.out.print("Number of rounds to play Between 5 and 10: ");
            String Line = state.Scanner.nextLine().trim();
            try { R = Integer.parseInt(Line); } catch (Exception e) { R = 0; }
        } while (R < 5 || R > 10);
        state.Rounds = R;
    }
}
