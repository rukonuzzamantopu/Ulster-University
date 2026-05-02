package org.example;

import java.util.Objects;

public class Card {
    public enum Suit {HEARTS, DIAMONDS, CLUBS, SPADES}

    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int value;
        Rank(int v) { this.value = v; }
        public int getValue() { return value; }
    }

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() { return rank; }
    public Suit getSuit() { return suit; }

    @Override
    public String toString() {
        String r;
        switch (rank) {
            case JACK: r = "J"; break;
            case QUEEN: r = "Q"; break;
            case KING: r = "K"; break;
            case ACE: r = "A"; break;
            default: r = String.valueOf(rank.getValue()); break;
        }
        String s;
        switch (suit) {
            case HEARTS: s = " of Hearts"; break;
            case DIAMONDS: s = " of Diamonds"; break;
            case CLUBS: s = " of Clubs"; break;
            default: s = " of Spades"; break;
        }
        return r + s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() { return Objects.hash(rank, suit); }
}

