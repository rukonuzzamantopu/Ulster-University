package org.Piyal;

public class Main {
    public static void main(String[] args) {


        GameState state = new GameState();

        SetupManager setup = new SetupManager();
        RoundManager rounds = new RoundManager();
        CollectionPrinter printer = new CollectionPrinter();
        AdjustmentManager adjust = new AdjustmentManager();
        BonusManager bonus = new BonusManager();
        ResultsManager results = new ResultsManager();

        System.out.println("---------------------------------------------");
        System.out.println("   HIGH CARD SERIES  >>  by Piyal         ");
        System.out.println("----------------------------------------------");
        System.out.println("                                               ");
        System.out.println("  Hey there! My name is Piyal.                  ");
        System.out.println("  I built this game just for you!               ");
        System.out.println("  Ready to play? Let us get started!            ");
        System.out.println("                                                ");
        System.out.println("-----------------------------------------------");
        System.out.println();
        System.out.println("[ Step 1 -- Who is playing? ]");
        setup.SetupPlayers(state);
        setup.SetupRounds(state);
        state.Deck.Shuffle();
        System.out.println("\nDeck created and shuffled. Commencing play...");
        rounds.PlayRounds(state);
        printer.ShowCollectionsAndScores(state);
        adjust.AdjustmentStage(state);
        bonus.ApplyBonuses(state);
        results.ShowFinalResults(state);
    }
}



