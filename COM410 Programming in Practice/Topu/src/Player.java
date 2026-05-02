package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final boolean computer;
    private final List<Card> collected = new ArrayList<>();
    private int roundPoints = 0;
    private int bonusPoints = 0;
    private boolean usedAdjustment = false;

    public Player(String name) {
        this.name = name;
        this.computer = "Computer".equalsIgnoreCase(name);
    }

    public String getName() { return name; }
    public boolean isComputer() { return computer; }
    public List<Card> getCollected() { return collected; }

    public void addCollected(Card c) { if (c != null) collected.add(c); }

    public int getRoundPoints() { return roundPoints; }
    public void addRoundPoints(int p) { roundPoints += p; }

    public int getBonusPoints() { return bonusPoints; }
    public void addBonusPoints(int p) { bonusPoints += p; }

    public boolean hasUsedAdjustment() { return usedAdjustment; }
    public void setUsedAdjustment(boolean v) { usedAdjustment = v; }

    public int getFinalScore() { return roundPoints + bonusPoints; }
}
