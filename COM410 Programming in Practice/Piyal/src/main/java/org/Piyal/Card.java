package org.Piyal;

import java.util.Objects;

/**
 * Simple representation of a playing card.
 * Holds a Rank and a Suit and provides textual formatting.
 */
public class Card {
    /** Card suits */
    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }

    /** Card ranks with numeric values used for comparisons */
    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int Value;
        Rank(int v) { this.Value = v; }
        public int GetValue() { return Value; }
    }

    // Fields (immutable)
    private final Rank RankVal;
    private final Suit SuitVal;

    /** Create a new card with the given rank and suit. */
    public Card(Rank rank, Suit suit) { this.RankVal = rank; this.SuitVal = suit; }

    /** Return the rank. */
    public Rank GetRank() { return RankVal; }

    /** Return the suit. */
    public Suit GetSuit() { return SuitVal; }

    /**
     * Human-readable representation, e.g. "A of Spades" or "10 of Hearts".
     */
    @Override
    public String toString() {
        String r;
        switch (RankVal) {
            case JACK: r = "J"; break;
            case QUEEN: r = "Q"; break;
            case KING: r = "K"; break;
            case ACE: r = "A"; break;
            default: r = String.valueOf(RankVal.GetValue()); break;
        }
        String s;
        switch (SuitVal) {
            case HEARTS: s = " of Hearts"; break;
            case DIAMONDS: s = " of Diamonds"; break;
            case CLUBS: s = " of Clubs"; break;
            default: s = " of Spades"; break;
        }
        return r + s;
    }

    /**
     * Equality uses rank and suit.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return RankVal == card.RankVal && SuitVal == card.SuitVal;
    }

    @Override
    public int hashCode() { return Objects.hash(RankVal, SuitVal); }
}



