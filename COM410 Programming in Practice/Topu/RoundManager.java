package org.example;

import java.util.*;

public class RoundManager {
    private final GameEngine engine;

    public RoundManager(GameEngine engine) {
        this.engine = engine;
    }

    public void conductAllRounds() {
        int numRounds = engine.getNumRounds();
        int rnd = 1;
        if (numRounds >= rnd) {
            do {
            engine.emit("[ Round " + rnd + " / " + numRounds + " ]  -- " + engine.AUTHOR + " deals!");
            engine.emit("  .................................................");

            Map<Participant, PlayingCard> dealMap = distributeCards();
            settleRound(dealMap);
            showStandings(rnd);

            engine.emit("");
            engine.emitInline("  >> Hit Enter when you are ready for the next round... ");
            engine.getScanner().nextLine();
            engine.emit("");
            rnd++;
        } while (rnd <= numRounds);
        }
    }

    private Map<Participant, PlayingCard> distributeCards() {
        Map<Participant, PlayingCard> dealMap = new LinkedHashMap<>();
        List<Participant> roster = engine.getRoster();
        CardPile pile = engine.getPile();
        int idx = 0;
        if (roster.size() > 0) {
            do {
                Participant  p    = roster.get(idx);
                PlayingCard  card = pile.pullTopCard();       // CardPile ADT operation
                dealMap.put(p, card);
                String cardText = (card != null) ? card.asText() : "[deck empty]";
                engine.emit("  " + p.tag() + "  got  >>  " + cardText);
                idx++;
            } while (idx < roster.size());
        }
        return dealMap;
    }

    private void settleRound(Map<Participant, PlayingCard> dealMap) {
        int peak = locatePeakStrength(dealMap);
        if (peak < 0) {
            engine.emit("  -- No valid cards in this round. Skipping. --");
            return;
        }

        List<Participant> topGroup = gatherTopGroup(dealMap, peak);
        pushLosingCardsToPile(dealMap, topGroup);

        engine.emit("");
        if (topGroup.size() == 1) {
            Participant champ = topGroup.get(0);
            champ.winCard(dealMap.get(champ));            // Participant ADT operation
            champ.grantRoundScore(3);                     // Participant ADT operation
            engine.emit("  *** WINNER this round : " + champ.tag()
                    + "  [+3 pts]  keeps  " + dealMap.get(champ).asText() + " ***");
        } else {
            engine.emitInline("  --- TIE  between :");
            int k = 0;
            if (topGroup.size() > 0) {
                do {
                    Participant tied = topGroup.get(k);
                    tied.winCard(dealMap.get(tied));           // Participant ADT operation
                    tied.grantRoundScore(1);                   // Participant ADT operation
                    engine.emitInline("  " + tied.tag());
                    k++;
                } while (k < topGroup.size());
            }
            engine.emit("  [+1 pt each] ---");
        }
    }

    private int locatePeakStrength(Map<Participant, PlayingCard> dealMap) {
        int peak = -1;
        Iterator<PlayingCard> it = dealMap.values().iterator();
        if (it.hasNext()) {
            do {
                PlayingCard c = it.next();
                if (c != null && c.fetchFaceValue() > peak)   // PlayingCard ADT operation
                    peak = c.fetchFaceValue();
            } while (it.hasNext());
        }
        return peak;
    }

    private List<Participant> gatherTopGroup(Map<Participant, PlayingCard> dealMap, int peak) {
        List<Participant> topGroup = new ArrayList<>();
        Iterator<Map.Entry<Participant, PlayingCard>> it = dealMap.entrySet().iterator();
        if (it.hasNext()) {
            do {
                Map.Entry<Participant, PlayingCard> entry = it.next();
                PlayingCard c = entry.getValue();
                if (c != null && c.fetchFaceValue() == peak)  // PlayingCard ADT operation
                    topGroup.add(entry.getKey());
            } while (it.hasNext());
        }
        return topGroup;
    }

    private void pushLosingCardsToPile(Map<Participant, PlayingCard> dealMap,
                                        List<Participant> topGroup) {
        CardPile pile = engine.getPile();
        Iterator<Map.Entry<Participant, PlayingCard>> it = dealMap.entrySet().iterator();
        if (it.hasNext()) {
            do {
                Map.Entry<Participant, PlayingCard> entry = it.next();
                if (!topGroup.contains(entry.getKey()))
                    pile.appendCard(entry.getValue());         // CardPile ADT operation
            } while (it.hasNext());
        }
    }

    private void showStandings(int rnd) {
        engine.emit("");
        engine.emit("  ..... Scoreboard  after  Round " + rnd + " .....");
        List<Participant> roster = engine.getRoster();
        int idx = 0;
        if (roster.size() > 0) {
            do {
                Participant p = roster.get(idx);
                engine.emit("        " + p.tag() + "  :  " + p.roundScore() + " pts");
                idx++;
            } while (idx < roster.size());
        }
    }
}
