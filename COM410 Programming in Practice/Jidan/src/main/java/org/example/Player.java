package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player in the game. Tracks collected cards and scoring.
 */
public class Player {

    private final String name;
    private final boolean computer;
    private final List<Card> collected = new ArrayList<>();
    private int roundPoints = 0;
    private int bonusPoints = 0;
    private boolean usedAdjustment = false;

    public Player(String name) {
        this.name = name;
        this.computer = name.equalsIgnoreCase("Computer");
    }

    /** Returns the player's display name. */
    public String getName() {
        return name;
    }

    /** True when this player is controlled by the computer. */
    public boolean isComputer() {
        return computer;
    }

    /** The list of cards this player has collected during play. */
    public List<Card> getCollected() {
        return collected;
    }

    /** Add a collected card to this player (ignores null). */
    public void addCollected(Card card) {
        if (card != null) collected.add(card);
    }

    /** Round points (sum of points earned per round). */
    public int getRoundPoints() {
        return roundPoints;
    }

    /** Add points earned in a round. */
    public void addRoundPoints(int points) {
        roundPoints += points;
    }

    /** Bonus points awarded after round scoring. */
    public int getBonusPoints() {
        return bonusPoints;
    }

    /** Add bonus points. */
    public void addBonusPoints(int points) {
        bonusPoints += points;
    }

    /** Whether the player already used the optional adjustment stage. */
    public boolean hasUsedAdjustment() {
        return usedAdjustment;
    }

    /** Mark whether the player has used the adjustment option. */
    public void setUsedAdjustment(boolean used) {
        usedAdjustment = used;
    }

    /** Compute final score = round points + bonus points. */
    public int getFinalScore() {
        return roundPoints + bonusPoints;
    }
}

