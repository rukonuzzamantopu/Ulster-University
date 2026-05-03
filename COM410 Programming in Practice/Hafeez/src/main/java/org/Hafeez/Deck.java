package org.Hafeez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ADT: Deck
 * -------------------------------------------------
 * A Deck represents an ordered collection of 52
 * playing cards. Cards can be dealt from the top
 * or returned to the bottom.
 *
 * ADT Operations:
 *   build()            -> fills deck with 52 cards
 *   shuffle()          -> randomises card order
 *   dealCard()         -> removes + returns top card
 *   returnCard(c)      -> places card at bottom
 *   returnCards(list)  -> places many cards at bottom
 *   isEmpty()          -> true if no cards remain
 *   size()             -> number of cards remaining
 * -------------------------------------------------
 */
public class Deck {

    // Internal data (hidden from outside)
    private final List<Card> cards = new ArrayList<>(52);

    // Constructor
    public Deck() {
        build();
    }

    // ADT Operations

    /**
     * ADT Operation: build()
     * Fills the deck with all 52 cards (13 ranks x 4 suits).
     * Internal logic: nested while loops over enum values.
     */
    private void build() {
        Card.Suit[] suits = Card.Suit.values();
        Card.Rank[] ranks = Card.Rank.values();
        int si = 0;
        while (si < suits.length) {
            int ri = 0;
            while (ri < ranks.length) {
                cards.add(new Card(ranks[ri], suits[si]));
                ri++;
            }
            si++;
        }
    }

    /**
     * ADT Operation: shuffle()
     * Randomises the order of cards in the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * ADT Operation: dealCard()
     * Removes and returns the top card of the deck.
     * Returns null if the deck is empty.
     */
    public Card dealCard() {
        return isEmpty() ? null : cards.remove(0);
    }

    /**
     * ADT Operation: returnCard(card)
     * Places a single card at the bottom of the deck.
     */
    public void returnCard(Card card) {
        if (card != null) cards.add(card);
    }

    /**
     * ADT Operation: returnCards(list)
     * Places multiple cards at the bottom of the deck.
     */
    public void returnCards(List<Card> list) {
        if (list == null) return;
        int i = 0;
        while (i < list.size()) {
            cards.add(list.get(i));
            i++;
        }
    }

    /**
     * ADT Operation: isEmpty()
     * Returns true if no cards remain in the deck.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * ADT Operation: size()
     * Returns the number of cards currently in the deck.
     */
    public int size() {
        return cards.size();
    }
}

