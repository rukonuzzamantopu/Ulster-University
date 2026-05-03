package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class CardPile {

    private final LinkedList<PlayingCard> pile = new LinkedList<>();

    public CardPile() {
        assemblePile();
    }

    private void assemblePile() {
        PlayingCard.SuitKind[] allSuits = PlayingCard.SuitKind.values();
        PlayingCard.PipKind[]  allPips  = PlayingCard.PipKind.values();

        int suitIdx = 0;
        if (allSuits.length > 0) {
            do {
                int pipIdx = 0;
                if (allPips.length > 0) {
                    do {
                        pile.addLast(new PlayingCard(allPips[pipIdx], allSuits[suitIdx]));
                        pipIdx++;
                    } while (pipIdx < allPips.length);
                }
                suitIdx++;
            } while (suitIdx < allSuits.length);
        }
    }

    

    /**
     * ADT Operation: scramble()
     * Randomises the order of all cards in the pile.
     * Internal logic: copy to ArrayList, shuffle, rebuild LinkedList.
     */
    public void scramble() {
        List<PlayingCard> tmp = new LinkedList<>(pile);
        Collections.shuffle(tmp);
        pile.clear();
        int idx = 0;
        if (tmp.size() > 0) {
            do {
                pile.addLast(tmp.get(idx));
                idx++;
            } while (idx < tmp.size());
        }
    }

    /**
     * ADT Operation: pullTopCard()
     * Removes and returns the front card.
     * Returns null when pile is exhausted.
     */
    public PlayingCard pullTopCard() {
        return exhausted() ? null : pile.pollFirst();
    }

    /**
     * ADT Operation: appendCard(card)
     * Places a single card at the back of the pile.
     */
    public void appendCard(PlayingCard card) {
        if (card != null) pile.addLast(card);
    }

    /**
     * ADT Operation: appendMany(cards)
     * Places a list of cards at the back of the pile.
     */
    public void appendMany(List<PlayingCard> cards) {
        if (cards == null) return;
        int idx = 0;
        if (cards.size() > 0) {
            do {
                pile.addLast(cards.get(idx));
                idx++;
            } while (idx < cards.size());
        }
    }

    /**
     * ADT Operation: cardCount()
     * Returns how many cards remain in the pile.
     */
    public int cardCount() {
        return pile.size();
    }

    /**
     * ADT Operation: exhausted()
     * Returns true when no cards remain.
     */
    public boolean exhausted() {
        return pile.isEmpty();
    }
}
