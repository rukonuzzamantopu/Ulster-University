package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a standard deck of 52 playing cards.
 *
 * Responsibilities:
 * - Initialize a full deck
 * - Shuffle cards
 * - Allow drawing cards from the top
 * - Return cards to the bottom of the deck
 */
public class Deck {

    // List storing all cards currently in the deck
    private final List<Card> cardList = new ArrayList<>();

    /**
     * Constructor that initializes the deck with all 52 cards.
     */
    public Deck() {

        for (Card.Suit suitType : Card.Suit.values()) {
            for (Card.Rank rankType : Card.Rank.values()) {
                cardList.add(new Card(rankType, suitType));
            }
        }
    }

    /**
     * Randomly shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cardList);
    }

    /**
     * Checks if the deck is empty.
     */
    public boolean isEmpty() {
        return cardList.isEmpty();
    }

    /**
     * Returns the number of cards remaining in the deck.
     */
    public int size() {
        return cardList.size();
    }

    /**
     * Draws and removes the top card from the deck.
     *
     * @return the top card or null if deck is empty
     */
    public Card drawTopCard() {

        if (cardList.isEmpty()) {
            return null;
        }

        return cardList.remove(0);
    }

    /**
     * Backwards-compatible wrapper: drawTop()
     */
    public Card drawTop() { return drawTopCard(); }

    /**
     * Returns a single card to the bottom of the deck.
     */
    public void addCardToBottom(Card card) {

        if (card != null) {
            cardList.add(card);
        }
    }

    /**
     * Backwards-compatible wrapper: returnToBottom(Card)
     */
    public void returnToBottom(Card card) { addCardToBottom(card); }

    /**
     * Returns multiple cards to the bottom of the deck.
     */
    public void addMultipleCardsToBottom(List<Card> returnedCards) {

        if (returnedCards != null) {
            cardList.addAll(returnedCards);
        }
    }

    /**
     * Backwards-compatible wrapper: returnManyToBottom(List<Card>)
     */
    public void returnManyToBottom(List<Card> returnedCards) { addMultipleCardsToBottom(returnedCards); }
}