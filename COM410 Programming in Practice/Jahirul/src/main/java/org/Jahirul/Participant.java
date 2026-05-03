package org.Jahirul;

import java.util.ArrayList;
import java.util.List;


public class Participant {

    private final String              tag;
    private final boolean             botControlled;
    private final List<PlayingCard>   wonPile      = new ArrayList<>();
    private int                       roundPts     = 0;
    private int                       bonusPts     = 0;
    private boolean                   swapUsed     = false;

    public Participant(String tag) {
        this.tag          = tag;
        this.botControlled = "Computer".equalsIgnoreCase(tag);
    }



    /** Returns the name tag of this participant */
    public String tag() { return tag; }

    /** Returns true if this participant is bot-controlled */
    public boolean botControlled() { return botControlled; }

    /**
     * ADT Operation: winCard(card)
     * Adds a won card to this participant's pile.
     */
    public void winCard(PlayingCard card) {
        if (card != null) wonPile.add(card);
    }

    /**
     * ADT Operation: peekWonPile()
     * Returns the internal list of won cards.
     * Encapsulation note: direct reference for game logic.
     */
    public List<PlayingCard> peekWonPile() { return wonPile; }

    /**
     * ADT Operation: wonPileSize()
     * Returns count of currently held won cards.
     */
    public int wonPileSize() { return wonPile.size(); }

    /**
     * ADT Operation: hasWonCards()
     * Returns true if at least one won card is held.
     */
    public boolean hasWonCards() { return !wonPile.isEmpty(); }

    /**
     * ADT Operation: extractCardAt(index)
     * Removes and returns the card at the given 0-based index.
     * Used during the adjustment/swap stage.
     */
    public PlayingCard extractCardAt(int index) {
        return wonPile.remove(index);
    }

    /**
     * ADT Operation: grantRoundScore(pts)
     * Adds points earned by winning a round.
     */
    public void grantRoundScore(int pts) { roundPts += pts; }

    /**
     * ADT Operation: grantBonusScore(pts)
     * Adds bonus points awarded at end of game.
     */
    public void grantBonusScore(int pts) { bonusPts += pts; }

    /** Returns round points accumulated so far */
    public int roundScore() { return roundPts; }

    /** Returns bonus points accumulated so far */
    public int bonusScore() { return bonusPts; }

    /**
     * ADT Operation: combinedScore()
     * Returns total of round + bonus points.
     */
    public int combinedScore() { return roundPts + bonusPts; }

    /**
     * ADT Operation: recordSwapOpportunityUsed()
     * Marks that this participant has used their one swap.
     */
    public void recordSwapOpportunityUsed() { swapUsed = true; }

    /**
     * ADT Operation: swapOpportunityTaken()
     * Returns true if this participant already used their swap.
     */
    public boolean swapOpportunityTaken() { return swapUsed; }
}
