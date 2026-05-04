package org.Piyal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameState {
    public final List<Player> Players;
    public final Deck Deck;
    public final Scanner Scanner;
    public int Rounds;

    public GameState() {
        this.Players = new ArrayList<>();
        this.Deck = new Deck();
        this.Scanner = new Scanner(System.in);
        this.Rounds = 5;
    }
}