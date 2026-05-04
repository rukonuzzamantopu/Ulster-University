package org.Piyal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> Cards = new ArrayList<>();

    public Deck() {
        for (Card.Suit S : Card.Suit.values()) {
            for (Card.Rank R : Card.Rank.values()) {
                Cards.add(new Card(R, S));
            }
        }
    }

    public void Shuffle() { Collections.shuffle(Cards); }

    public boolean IsEmpty() { return Cards.isEmpty(); }

    public int Size() { return Cards.size(); }

    public Card DrawTop() { if (Cards.isEmpty()) return null; return Cards.remove(0); }

    public void ReturnToBottom(Card C) { if (C != null) Cards.add(C); }

    public void ReturnManyToBottom(List<Card> ListCards) { if (ListCards != null) Cards.addAll(ListCards); }
}


