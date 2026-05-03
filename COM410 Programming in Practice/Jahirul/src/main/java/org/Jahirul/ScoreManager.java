package org.Jahirul;

import java.util.*;

public class ScoreManager {
    private final GameEngine engine;

    public ScoreManager(GameEngine engine) {
        this.engine = engine;
    }

    public void displayWonPiles() {
        engine.emit("[ Won Card Collections + Round Scores ]");
        engine.emit("  .................................................");
        List<Participant> roster = engine.getRoster();
        int idx = 0;
        if (roster.size() > 0) {
            do {
                Participant p = roster.get(idx);
                engine.emit("");
                engine.emit("  Participant  :  " + p.tag());
                engine.emit("  Round Score  :  " + p.roundScore() + " pts");
                engine.emit("  Won Cards    :");
                if (!p.hasWonCards()) {                        // Participant ADT operation
                    engine.emit("    -- none --");
                } else {
                    int ci = 0;
                    if (p.wonPileSize() > 0) {
                        do {
                            engine.emit("    (" + (ci + 1) + ")  " + p.peekWonPile().get(ci).asText());
                            ci++;
                        } while (ci < p.wonPileSize());
                    }
                }
                idx++;
            } while (idx < roster.size());
        }
        engine.emit("");
    }

    public void computeBonuses() {
        engine.emit("[ Bonus Calculation ]");
        engine.emit("  " + engine.AUTHOR + " checks everyone's won pile for bonuses...");
        engine.emit("  .................................................");
        engine.emit("");
        tallyRunBonus();
        tallySuitBonus();
    }

    private void tallyRunBonus() {
        engine.emit("  >> Consecutive Run Bonus:");
        int topLen = 0;
        Map<Participant, Integer> runLengths = new LinkedHashMap<>();

        int idx = 0;
        List<Participant> roster = engine.getRoster();
        if (roster.size() > 0) {
            do {
                Participant p   = roster.get(idx);
                int         len = measureLongestRun(p.peekWonPile());  // uses PlayingCard ADT
                runLengths.put(p, len);
                if (len > topLen) topLen = len;
                engine.emit("     " + p.tag() + "  ->  longest run = " + len);
                idx++;
            } while (idx < roster.size());
        }
        engine.emit("");
        distributeBonus(runLengths, topLen, "Run");
    }

    private void tallySuitBonus() {
        engine.emit("  >> Suit Concentration Bonus:");
        int topCount = 0;
        Map<Participant, Integer> suitCounts = new LinkedHashMap<>();

        int idx = 0;
        List<Participant> roster = engine.getRoster();
        if (roster.size() > 0) {
            do {
                Participant p     = roster.get(idx);
                int         count = measureTopSuitCount(p.peekWonPile()); // uses PlayingCard ADT
                suitCounts.put(p, count);
                if (count > topCount) topCount = count;
                engine.emit("     " + p.tag() + "  ->  top suit count = " + count);
                idx++;
            } while (idx < roster.size());
        }
        engine.emit("");
        distributeBonus(suitCounts, topCount, "Suit");
    }

    private int measureLongestRun(List<PlayingCard> wonPile) {
        if (wonPile.isEmpty()) return 0;
        boolean[] seen = new boolean[15];
        int ci = 0;
        if (wonPile.size() > 0) {
            do {
                seen[wonPile.get(ci).fetchFaceValue()] = true;  // PlayingCard ADT operation
                ci++;
            } while (ci < wonPile.size());
        }
        int longest = 0, streak = 0, rank = 2;
        if (rank <= 14) {
            do {
                streak  = seen[rank] ? streak + 1 : 0;
                if (streak > longest) longest = streak;
                rank++;
            } while (rank <= 14);
        }
        return longest;
    }

    private int measureTopSuitCount(List<PlayingCard> wonPile) {
        int[] tally = new int[PlayingCard.SuitKind.values().length];
        int ci = 0;
        if (wonPile.size() > 0) {
            do {
                tally[wonPile.get(ci).fetchSuitTag().ordinal()]++;  // PlayingCard ADT operation
                ci++;
            } while (ci < wonPile.size());
        }
        int peak = 0, si = 0;
        if (si < tally.length) {
            do {
                if (tally[si] > peak) peak = tally[si];
                si++;
            } while (si < tally.length);
        }
        return peak;
    }

    private void distributeBonus(Map<Participant, Integer> scoreMap,
                                 int topValue, String bonusLabel) {
        if (topValue <= 0) return;
        List<Participant> leaders = new ArrayList<>();
        Iterator<Map.Entry<Participant, Integer>> it = scoreMap.entrySet().iterator();
        if (it.hasNext()) {
            do {
                Map.Entry<Participant, Integer> entry = it.next();
                if (entry.getValue() == topValue) leaders.add(entry.getKey());
            } while (it.hasNext());
        }

        if (leaders.size() == 1) {
            leaders.get(0).grantBonusScore(5);             // Participant ADT operation
            engine.emit("  *** " + leaders.get(0).tag()
                    + " wins the " + bonusLabel + " bonus  >> +5 pts ***");
        } else {
            int li = 0;
            if (leaders.size() > 0) {
                do {
                    leaders.get(li).grantBonusScore(2);        // Participant ADT operation
                    li++;
                } while (li < leaders.size());
            }
            engine.emitInline("  ** " + bonusLabel + " bonus TIED between :");
            int lj = 0;
            if (leaders.size() > 0) {
                do {
                    engine.emitInline("  " + leaders.get(lj).tag());
                    lj++;
                } while (lj < leaders.size());
            }
            engine.emit("  [+2 pts each] **");
        }
        engine.emit("");
    }

    public void announceResults() {
        engine.emit("**************************************************");
        engine.emit("*              FINAL RESULTS                     *");
        engine.emit("**************************************************");
        engine.emit("  " + engine.AUTHOR + " reveals the final standings :\n");

        List<Participant> roster = engine.getRoster();
        roster.sort((a, b) -> b.combinedScore() - a.combinedScore()); // Participant ADT

        int position = 1;
        int ri = 0;
        if (roster.size() > 0) {
            do {
                Participant p = roster.get(ri);
                engine.emit("  Position #" + position
                        + "  |  " + p.tag()
                        + "  |  Round : " + p.roundScore()
                        + "  |  Bonus : " + p.bonusScore()
                        + "  |  Total : " + p.combinedScore());    // Participant ADT operation
                position++;
                ri++;
            } while (ri < roster.size());
        }

        engine.emit("");
        int goldScore = roster.isEmpty() ? 0 : roster.get(0).combinedScore();
        List<Participant> goldGroup = new ArrayList<>();
        int gi = 0;
        if (roster.size() > 0) {
            do {
                if (roster.get(gi).combinedScore() == goldScore)
                    goldGroup.add(roster.get(gi));
                gi++;
            } while (gi < roster.size());
        }

        if (goldGroup.size() == 1) {
            engine.emit("  >>>  CHAMPION  :  " + goldGroup.get(0).tag() + "  <<<");
            engine.emit("  " + engine.AUTHOR + " congratulates you. Well deserved!");
        } else {
            engine.emitInline("  >>>  DRAW  between :");
            int di = 0;
            if (goldGroup.size() > 0) {
                do {
                    engine.emitInline("  " + goldGroup.get(di).tag());
                    di++;
                } while (di < goldGroup.size());
            }
            engine.emit("  <<<");
            engine.emit("  " + engine.AUTHOR + " says : brilliant effort from everyone!");
        }
        engine.emit("");
    }
}

