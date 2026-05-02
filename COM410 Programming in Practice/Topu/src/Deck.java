package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        for (Card.Suit s : Card.Suit.values()) {
            for (Card.Rank r : Card.Rank.values()) {
                cards.add(new Card(r, s));
            }
        }
    }

    public void shuffle() { Collections.shuffle(cards); }

    public boolean isEmpty() { return cards.isEmpty(); }

    public int size() { return cards.size(); }

    public Card drawTop() {
        if (cards.isEmpty()) return null;
        return cards.remove(0);
    }

    public void returnToBottom(Card c) { if (c != null) cards.add(c); }

    public void returnManyToBottom(List<Card> list) { if (list != null) cards.addAll(list); }
}

