package org.example;


import java.util.Map;
import java.util.Objects;

/**
 * Represents a standard playing card with a `Rank` and `Suit`.
 * <p>
 * Instances are immutable and provide basic utilities such as string
 * representation, equality, and hashing.
 */
public class Card {

    /** Suits available in a standard 52-card deck. */
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    /** Ranks available in a standard 52-card deck; each has an integer value. */
    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int value;

        /** Create a Rank with its numeric value. */
        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static final Map<Rank, String> RANK_SYMBOLS = Map.of(
            Rank.JACK, "J",
            Rank.QUEEN, "Q",
            Rank.KING, "K",
            Rank.ACE, "A"
    );

    private static final Map<Suit, String> SUIT_NAMES = Map.of(
            Suit.HEARTS, " of Hearts",
            Suit.DIAMONDS, " of Diamonds",
            Suit.CLUBS, " of Clubs",
            Suit.SPADES, " of Spades"
    );

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        // Prefer symbol for face cards, otherwise show numeric value
        String rankPart = RANK_SYMBOLS.getOrDefault(rank, String.valueOf(rank.getValue()));
        String suitPart = SUIT_NAMES.get(suit);
        return rankPart + suitPart;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card otherCard = (Card) obj;
        // Two cards are equal when both rank and suit match
        return rank == otherCard.rank && suit == otherCard.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
