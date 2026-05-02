package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the High Card Series game.
 *
 * Responsibilities:
 * - Store player identity (name, type)
 * - Track collected cards
 * - Manage scoring (round + bonus points)
 * - Handle adjustment usage status
 */
public class Player {

    // Name of the player
    private final String playerName;

    // Indicates whether the player is controlled by the computer
    private final boolean isComputerPlayer;

    // List of cards collected by the player during the game
    private final List<Card> collectedCards = new ArrayList<>();

    // Points earned during rounds
    private int roundScore = 0;

    // Bonus points earned after game evaluation
    private int bonusScore = 0;

    // Tracks whether the player has used the adjustment feature
    private boolean adjustmentUsed = false;

    /**
     * Constructor to create a player with a given name.
     * Automatically determines if the player is a computer.
     */
    public Player(String nameInput) {
        this.playerName = nameInput;
        this.isComputerPlayer = "Computer".equalsIgnoreCase(nameInput);
    }

    public String getName() {
        return playerName;
    }

    public boolean isComputer() {
        return isComputerPlayer;
    }

    public List<Card> getCollected() {
        return collectedCards;
    }

    /**
     * Adds a card to the player's collection.
     */
    public void addCollectedCard(Card newCard) {
        if (newCard != null) {
            collectedCards.add(newCard);
        }
    }

    // Backwards-compatible wrapper: some callers use addCollected(Card)
    public void addCollected(Card newCard) {
        addCollectedCard(newCard);
    }

    public int getRoundPoints() {
        return roundScore;
    }

    /**
     * Adds points earned in a round.
     */
    public void addRoundPoints(int points) {
        roundScore += points;
    }

    public int getBonusPoints() {
        return bonusScore;
    }

    /**
     * Adds bonus points after evaluation.
     */
    public void addBonusPoints(int points) {
        bonusScore += points;
    }

    public boolean hasUsedAdjustment() {
        return adjustmentUsed;
    }

    /**
     * Sets whether the player has used the adjustment option.
     */
    public void setAdjustmentUsed(boolean status) {
        adjustmentUsed = status;
    }

    // Backwards-compatible wrapper: some callers use setUsedAdjustment(boolean)
    public void setUsedAdjustment(boolean status) {
        setAdjustmentUsed(status);
    }

    /**
     * Calculates the final score of the player.
     */
    public int getFinalScore() {
        return roundScore + bonusScore;
    }
}
