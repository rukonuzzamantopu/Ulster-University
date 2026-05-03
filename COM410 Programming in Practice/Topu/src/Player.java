import java.util.ArrayList;
import java.util.List;

/**
 * ADT: Player
 * -------------------------------------------------
 * A Player represents a game participant who holds
 * a collection of won cards and accumulates points.
 *
 * ADT Operations:
 *   getName()          -> returns player name
 *   isComputer()       -> true if AI-controlled
 *   collectCard(c)     -> adds card to hand
 *   getHand()          -> returns list of held cards
 *   handSize()         -> number of cards in hand
 *   hasCards()         -> true if hand is not empty
 *   removeCardAt(i)    -> removes + returns card at index
 *   addRoundPoints(n)  -> increases round score
 *   addBonusPoints(n)  -> increases bonus score
 *   getRoundPoints()   -> returns round score
 *   getBonusPoints()   -> returns bonus score
 *   getTotalScore()    -> returns round + bonus
 *   markAdjustmentUsed()  -> records adjustment was used
 *   hasUsedAdjustment()   -> true if already adjusted
 * -------------------------------------------------
 */
public class Player {

    //Internal data (hidden from outside)
    private final String     name;
    private final boolean    computer;
    private final List<Card> hand           = new ArrayList<>();
    private int              roundPoints    = 0;
    private int              bonusPoints    = 0;
    private boolean          usedAdjustment = false;

    // Constructor 
    public Player(String name) {
        this.name     = name;
        this.computer = "Computer".equalsIgnoreCase(name);
    }

    //ADT Operations 

    /** Returns the name of this player */
    public String getName() {
        return name;
    }

    /** Returns true if this player is computer-controlled */
    public boolean isComputer() {
        return computer;
    }

    /**
     * ADT Operation: collectCard(card)
     * Adds a won card to this player's hand.
     */
    public void collectCard(Card card) {
        if (card != null) hand.add(card);
    }

    /**
     * ADT Operation: getHand()
     * Returns the list of cards currently held by this player.
     * Encapsulation: returns the internal list directly for game logic use.
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * ADT Operation: handSize()
     * Returns how many cards this player currently holds.
     */
    public int handSize() {
        return hand.size();
    }

    /**
     * ADT Operation: hasCards()
     * Returns true if this player holds at least one card.
     */
    public boolean hasCards() {
        return !hand.isEmpty();
    }

    /**
     * ADT Operation: removeCardAt(index)
     * Removes and returns the card at the given 0-based index.
     */
    public Card removeCardAt(int index) {
        return hand.remove(index);
    }

    /**
     * ADT Operation: addRoundPoints(points)
     * Adds points earned from winning a round.
     */
    public void addRoundPoints(int points) {
        roundPoints += points;
    }

    /**
     * ADT Operation: addBonusPoints(points)
     * Adds bonus points earned at end of game.
     */
    public void addBonusPoints(int points) {
        bonusPoints += points;
    }

    /** Returns points earned from rounds */
    public int getRoundPoints() {
        return roundPoints;
    }

    /** Returns bonus points earned */
    public int getBonusPoints() {
        return bonusPoints;
    }

    /**
     * ADT Operation: getTotalScore()
     * Returns the combined round + bonus score.
     */
    public int getTotalScore() {
        return roundPoints + bonusPoints;
    }

    /**
     * ADT Operation: markAdjustmentUsed()
     * Records that this player has used their one adjustment.
     */
    public void markAdjustmentUsed() {
        usedAdjustment = true;
    }

    /**
     * ADT Operation: hasUsedAdjustment()
     * Returns true if this player has already used their adjustment.
     */
    public boolean hasUsedAdjustment() {
        return usedAdjustment;
    }

    // kept for backward compatibility with Main 
    /** @deprecated use getHand() */
    public List<Card> getCollected()           { return getHand(); }
    /** @deprecated use collectCard() */
    public void addCollected(Card c)           { collectCard(c); }
    /** @deprecated use getTotalScore() */
    public int getFinalScore()                 { return getTotalScore(); }
    /** @deprecated use markAdjustmentUsed() */
    public void setUsedAdjustment(boolean v)   { if (v) markAdjustmentUsed(); }
}
