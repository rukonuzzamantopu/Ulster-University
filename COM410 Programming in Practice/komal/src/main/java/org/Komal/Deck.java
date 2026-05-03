package org.Komal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> CARDS;

    public Deck() {
        CARDS = BUILD_FRESH_DECK();
    }

    private static List<Card> BUILD_FRESH_DECK() {
        List<Card> fresh = new ArrayList<>(52);
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                fresh.add(new Card(rank, suit));
            }
        }
        return fresh;
    }

    public void SHUFFLE() {
        Collections.shuffle(CARDS);
    }

    public boolean IS_EMPTY() {
        return CARDS.isEmpty();
    }

    public int SIZE() {
        return CARDS.size();
    }

    public Card DRAW_TOP() {
        return IS_EMPTY() ? null : CARDS.remove(0);
    }

    public void RETURN_TO_BOTTOM(Card c) {
        if (c != null) CARDS.add(c);
    }

    public void RETURN_MANY_TO_BOTTOM(List<Card> list) {
        if (list != null) CARDS.addAll(list);
    }
}
