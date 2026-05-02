package org.example;


import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents a standard deck of playing cards backed by a deque.
 * <p>
 * Provides operations to shuffle, draw from the top, and return cards
 * to the bottom of the deck. Methods are defensive about nulls.
 */
public class Deck {

    /** Internal deque storing cards; front = top, back = bottom. */
    private final Deque<Card> cardDeque = new ArrayDeque<>();

    /** Build a new ordered 52-card deck (all suits × ranks). */
    public Deck() {
        Arrays.stream(Card.Suit.values())
                .flatMap(suit -> Arrays.stream(Card.Rank.values())
                        .map(rank -> new Card(rank, suit)))
                .forEach(cardDeque::addLast);
    }

    /** Randomize the order of cards in the deck. */
    public void shuffleDeck() {
        List<Card> temp = cardDeque.stream().collect(Collectors.toList());
        Collections.shuffle(temp);
        cardDeque.clear();
        // Preserve shuffled order by adding to the tail
        temp.forEach(cardDeque::addLast);
    }

    /** Returns true when no cards remain in the deck. */
    public boolean isDeckEmpty() {
        return cardDeque.isEmpty();
    }

    /** Returns the number of cards currently in the deck. */
    public int deckSize() {
        return cardDeque.size();
    }

    /**
     * Draw (remove) the top card. Returns null when the deck is empty.
     * Top corresponds to the head of the internal deque.
     */
    public Card drawTopCard() {
        return cardDeque.isEmpty() ? null : cardDeque.pollFirst();
    }

    /** Return a single card to the bottom of the deck (ignores null). */
    public void returnCardToBottom(Card card) {
        if (card != null) cardDeque.addLast(card);
    }

    /** Return multiple cards to the bottom in the order provided. */
    public void returnCardsToBottom(List<Card> cards) {
        if (cards != null) cards.forEach(cardDeque::addLast);
    }
}

