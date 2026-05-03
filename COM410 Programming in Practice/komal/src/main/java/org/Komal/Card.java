package org.Komal;

import java.util.Objects;

public class Card {

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES;
        public String DISPLAY() {
            String n = name();
            return " of " + n.charAt(0) + n.substring(1).toLowerCase();
        }
    }

    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int VALUE;

        Rank(int v) { this.VALUE = v; }

        public int GET_VALUE() { return VALUE; }

        public String DISPLAY() {
            return VALUE <= 10 ? String.valueOf(VALUE) : String.valueOf(name().charAt(0));
        }
    }

    private final Rank RANK;
    private final Suit SUIT;

    public Card(Rank RANK, Suit SUIT) {
        this.RANK = RANK;
        this.SUIT = SUIT;
    }

    public Rank GET_RANK() { return RANK; }
    public Suit GET_SUIT() { return SUIT; }

    @Override
    public String toString() {
        return RANK.DISPLAY() + SUIT.DISPLAY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card c = (Card) o;
        return RANK == c.RANK && SUIT == c.SUIT;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RANK, SUIT);
    }
}

