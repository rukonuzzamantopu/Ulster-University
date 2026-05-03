package org.Junaeid;

import java.util.Objects;

/**
 * Represents a single playing card with a rank and suit.
 */
public class Card {

    // Enum representing the four suits in a standard deck
    public enum Suit {HEARTS, DIAMONDS, CLUBS, SPADES}

    // Enum representing card ranks with their numeric values
    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
        SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int numericValue;

        Rank(int value) {
            this.numericValue = value;
        }

        public int getValue() {
            return numericValue;
        }
    }

    // Rank of the card (e.g., Ace, King)
    private final Rank cardRank;

    // Suit of the card (e.g., Hearts, Spades)
    private final Suit cardSuit;

    /**
     * Constructor to create a card with a specific rank and suit.
     */
    public Card(Rank rank, Suit suit) {
        this.cardRank = rank;
        this.cardSuit = suit;
    }

    public Rank getRank() {
        return cardRank;
    }

    public Suit getSuit() {
        return cardSuit;
    }

    /**
     * Returns a readable string representation of the card.
     * Example: "A of Spades", "10 of Hearts"
     */
    @Override
    public String toString() {

        String rankSymbol;

        switch (cardRank) {
            case JACK: rankSymbol = "J"; break;
            case QUEEN: rankSymbol = "Q"; break;
            case KING: rankSymbol = "K"; break;
            case ACE: rankSymbol = "A"; break;
            default: rankSymbol = String.valueOf(cardRank.getValue());
        }

        String suitName;

        switch (cardSuit) {
            case HEARTS: suitName = " of Hearts"; break;
            case DIAMONDS: suitName = " of Diamonds"; break;
            case CLUBS: suitName = " of Clubs"; break;
            default: suitName = " of Spades";
        }

        return rankSymbol + suitName;
    }

    /**
     * Checks if two cards are equal based on rank and suit.
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof Card)) return false;

        Card otherCard = (Card) otherObject;

        return cardRank == otherCard.cardRank &&
                cardSuit == otherCard.cardSuit;
    }

    /**
     * Generates hash code for the card (used in collections like HashSet).
     */
    @Override
    public int hashCode() {
        return Objects.hash(cardRank, cardSuit);
    }
}