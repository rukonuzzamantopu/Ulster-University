package org.Jahirul;

import java.util.Objects;


public class PlayingCard {

    /*
     * ---- Inner ADT : SuitKind ----
     * Data structure: enum with a stored String field.
     */
    public enum SuitKind {
        CLUB   ("Clubs"),
        DIAMOND("Diamonds"),
        HEART  ("Hearts"),
        SPADE  ("Spades");

        private final String title;
        SuitKind(String t) { this.title = t; }

        /** Returns the full display title of this suit */
        public String fetchTitle() { return title; }
    }

    /*
     * ---- Inner ADT : PipKind ----
     * Data structure: enum with strength + label fields.
     */
    public enum PipKind {
        TWO  ( 2,  "2"),  THREE( 3,  "3"),  FOUR ( 4,  "4"),
        FIVE ( 5,  "5"),  SIX  ( 6,  "6"),  SEVEN( 7,  "7"),
        EIGHT( 8,  "8"),  NINE ( 9,  "9"),  TEN  (10, "10"),
        JACK (11,  "J"),  QUEEN(12,  "Q"),  KING (13,  "K"),
        ACE  (14,  "A");

        private final int    strength;
        private final String pipLabel;

        PipKind(int s, String l) { this.strength = s; this.pipLabel = l; }

        public int    fetchStrength() { return strength; }
        public String fetchPipLabel() { return pipLabel; }
    }

    private final PipKind  pip;
    private final SuitKind suit;

    public PlayingCard(PipKind pip, SuitKind suit) {
        this.pip  = pip;
        this.suit = suit;
    }


    public int       fetchFaceValue() { return pip.fetchStrength();  }
    public SuitKind  fetchSuitTag()   { return suit;                 }
    public String    fetchPipLabel()  { return pip.fetchPipLabel();  }

    public boolean isStrongerThan(PlayingCard other) {
        return other == null || fetchFaceValue() > other.fetchFaceValue();
    }

    public boolean sameStrengthAs(PlayingCard other) {
        return other != null && fetchFaceValue() == other.fetchFaceValue();
    }

    public String asText() {
        return pip.fetchPipLabel() + " of " + suit.fetchTitle();
    }

    @Override public String  toString()            { return asText(); }
    @Override public boolean equals(Object obj)    {
        if (this == obj) return true;
        if (!(obj instanceof PlayingCard)) return false;
        PlayingCard o = (PlayingCard) obj;
        return pip == o.pip && suit == o.suit;
    }
    @Override public int hashCode() { return Objects.hash(pip, suit); }
}

