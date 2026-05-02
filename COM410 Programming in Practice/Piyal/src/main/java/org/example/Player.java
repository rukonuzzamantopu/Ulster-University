package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final boolean Computer;
    private final List<Card> Collected = new ArrayList<>();
    private int RoundPoints = 0;
    private int BonusPoints = 0;
    private boolean AdjustmentUsed = false;

    public Player(String NameInput) {
        this.name = NameInput;
        this.Computer = "Computer".equalsIgnoreCase(NameInput);
    }

    public String GetName() { return name; }
    public boolean IsComputer() { return Computer; }
    public List<Card> GetCollected() { return Collected; }

    public void AddCollectedCard(Card NewCard) { if (NewCard != null) Collected.add(NewCard); }

    public int GetRoundPoints() { return RoundPoints; }
    public void AddRoundPoints(int Points) { RoundPoints += Points; }

    public int GetBonusPoints() { return BonusPoints; }
    public void AddBonusPoints(int Points) { BonusPoints += Points; }

    public boolean HasUsedAdjustment() { return AdjustmentUsed; }
    public void SetAdjustmentUsed(boolean Status) { AdjustmentUsed = Status; }

    public int GetFinalScore() { return RoundPoints + BonusPoints; }

    // Backwards-compatible wrappers for older GameApp API
    public void AddCollected(Card NewCard) { AddCollectedCard(NewCard); }
    public void SetUsedAdjustment(boolean Status) { SetAdjustmentUsed(Status); }
}
