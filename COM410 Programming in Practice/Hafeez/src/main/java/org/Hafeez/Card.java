package org.Hafeez;

import java.util.Objects;

/**
 * ADT: Card
 * -------------------------------------------------
 * A Card represents a single playing card with a
 * Rank and a Suit. It is immutable once created.
 *
 * ADT Operations:
 *   getRank()    -> returns the rank of the card
 *   getSuit()    -> returns the suit of the card
 *   getValue()   -> returns the numeric rank value
 *   display()    -> returns human-readable string
 *   equals()     -> compares two cards by rank+suit
 * -------------------------------------------------
 */
public class Card {

    // ─ Inner ADT: Suit
    public enum Suit {
        HEARTS("Hearts"),
        DIAMONDS("Diamonds"),
        CLUBS("Clubs"),
        SPADES("Spades");

        private final String label;

        Suit(String label) {
            this.label = label;
        }

        /** ADT Operation: returns display name of this suit */
        public String getLabel() {
            return label;
        }
    }

    //Inner ADT: Rank
    public enum Rank {
        TWO(2,  "2"),  THREE(3,  "3"), FOUR(4,   "4"),
        FIVE(5, "5"),  SIX(6,   "6"), SEVEN(7,  "7"),
        EIGHT(8,"8"),  NINE(9,  "9"), TEN(10,  "10"),
        JACK(11,"J"),  QUEEN(12,"Q"), KING(13,  "K"),
        ACE(14, "A");

        private final int    value;
        private final String symbol;

        Rank(int value, String symbol) {
            this.value  = value;
            this.symbol = symbol;
        }

        /** ADT Operation: returns numeric value used for comparison */
        public int getValue() {
            return value;
        }

        /** ADT Operation: returns short display symbol e.g. "J", "10", "A" */
        public String getSymbol() {
            return symbol;
        }
    }

    // ── Internal data (hidden from outside)
    private final Rank rank;
    private final Suit suit;

    // ── Constructor
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    // ── ADT Operations

    /** Returns the rank of this card */
    public Rank getRank() {
        return rank;
    }

    /** Returns the suit of this card */
    public Suit getSuit() {
        return suit;
    }

    /** Returns the numeric value of this card's rank */
    public int getValue() {
        return rank.getValue();
    }

    /** Returns true if this card beats the other card by rank value */
    public boolean beats(Card other) {
        return other == null || this.getValue() > other.getValue();
    }

    /** Returns true if this card ties with the other card by rank value */
    public boolean tiesWith(Card other) {
        return other != null && this.getValue() == other.getValue();
    }

    /** Returns human-readable display e.g. "A of Spades" */
    @Override
    public String toString() {
        return rank.getSymbol() + " of " + suit.getLabel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card c = (Card) o;
        return rank == c.rank && suit == c.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}

