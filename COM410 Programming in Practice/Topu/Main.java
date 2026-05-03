package org.example;


public class Main {
    public static void main(String[] args) {


        GameState state = new GameState();

        SetupManager setup = new SetupManager();
        RoundManager rounds = new RoundManager();
        CollectionPrinter printer = new CollectionPrinter();
        AdjustmentManager adjust = new AdjustmentManager();
        BonusManager bonus = new BonusManager();
        ResultsManager results = new ResultsManager();

        System.out.println("     High Card Series ");
        setup.SetupPlayers(state);
        setup.SetupRounds(state);
        state.Deck.Shuffle();
        rounds.PlayRounds(state);
        printer.ShowCollectionsAndScores(state);
        adjust.AdjustmentStage(state);
        bonus.ApplyBonuses(state);
        results.ShowFinalResults(state);
    }
}


