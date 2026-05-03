package org.Komal;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private static final String COMPUTER_NAME = "Computer";

    private final String NAME;
    private final boolean COMPUTER;
    private final List<Card> COLLECTED = new ArrayList<>();
    private int ROUND_POINTS  = 0;
    private int BONUS_POINTS  = 0;
    private boolean USED_ADJUSTMENT = false;

    public Player(String NAME) {
        this.NAME     = NAME;
        this.COMPUTER = COMPUTER_NAME.equalsIgnoreCase(NAME);
    }

    public String       GET_NAME()            { return NAME; }
    public boolean      IS_COMPUTER()         { return COMPUTER; }
    public List<Card>   GET_COLLECTED()       { return COLLECTED; }
    public int          GET_ROUND_POINTS()     { return ROUND_POINTS; }
    public int          GET_BONUS_POINTS()     { return BONUS_POINTS; }
    public boolean      HAS_USED_ADJUSTMENT()  { return USED_ADJUSTMENT; }
    public int          GET_FINAL_SCORE()      { return ROUND_POINTS + BONUS_POINTS; }

    public void ADD_COLLECTED(Card c)          { if (c != null) COLLECTED.add(c); }
    public void ADD_ROUND_POINTS(int p)         { ROUND_POINTS += p; }
    public void ADD_BONUS_POINTS(int p)         { BONUS_POINTS += p; }
    public void SET_USED_ADJUSTMENT(boolean v)  { USED_ADJUSTMENT = v; }
}
