package org.Jahirul;

import java.util.*;


public class GameEngine {
    public static final String AUTHOR = "Jahirul";

    private final List<Participant> roster  = new ArrayList<>();
    private final CardPile          pile    = new CardPile();
    private final Scanner           kbd     = new Scanner(System.in);
    private int                     numRounds = 5;



    /**
     * ADT Operation: launch()
     * Executes every phase of the game in sequence.
     */
    public void launch() {
        openingMessage();
        registerParticipants();
        chooseRoundCount();
        pile.scramble();                   // CardPile ADT operation

        RoundManager roundManager = new RoundManager(this);
        ScoreManager scoreManager = new ScoreManager(this);
        SwapManager  swapManager  = new SwapManager(this);

        roundManager.conductAllRounds();
        scoreManager.displayWonPiles();
        swapManager.conductSwapStage();
        scoreManager.computeBonuses();
        scoreManager.announceResults();

        closingMessage();
    }

    /* Accessors for manager classes */
    public List<Participant> getRoster() { return roster; }
    public CardPile getPile() { return pile; }
    public Scanner getScanner() { return kbd; }
    public int getNumRounds() { return numRounds; }

    private void openingMessage() {
        emit("**************************************************");
        emit("*    HIGH CARD SERIES  >>  by " + AUTHOR + "          *");
        emit("**************************************************");
        emit("*                                                *");
        emit("*  Hey there! My name is " + AUTHOR + ".           *");
        emit("*  I built this game just for you!              *");
        emit("*  Ready to play? Let us get started!           *");
        emit("*                                                *");
        emit("**************************************************");
        emit("");
    }

    private void closingMessage() {
        emit("");
        emit("**************************************************");
        emit("*  Game over! " + AUTHOR + " thanks you for playing.  *");
        emit("*  Hope you had a great time. See you next time! *");
        emit("**************************************************");
    }

    /** Single output method -- all printing goes through here */
    public void emit(String line) { System.out.println(line); }

    public void emitInline(String text) { System.out.print(text); }


    /**
     * ADT Operation: registerParticipants()
     * Builds the Participant ADT roster.
     * Input: player count (2-5), then name per participant.
     */
    private void registerParticipants() {
        emit("[ Step 1 -- Who is playing? ]");
        int total = grabNumber("  How many players will join? (2 to 5): ", 2, 5);

        int slot = 1;
        if (total >= slot) {
            do {
                emitInline("  Enter name for player " + slot + ": ");
                String enteredName = kbd.nextLine().trim();
                if (enteredName.isEmpty()) enteredName = "Player" + slot;
                roster.add(new Participant(enteredName));    // Participant ADT
                emit("  >> Welcome aboard, " + enteredName + "! Good luck!");
                slot++;
            } while (slot <= total);
        }
        emit("");
    }

    private void chooseRoundCount() {
        emit("[ Step 2 -- How many rounds? ]");
        numRounds = grabNumber("  Enter number of rounds (5 to 10): ", 5, 10);
        emit("  >> " + AUTHOR + " has prepared " + numRounds + " rounds. Here we go!");
        emit("");
    }


    public String handAsString(List<PlayingCard> hand) {
        if (hand.isEmpty()) return "-- none --";
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        if (hand.size() > 0) {
            do {
                if (idx > 0) sb.append(" | ");
                sb.append(hand.get(idx).asText());
                idx++;
            } while (idx < hand.size());
        }
        return sb.toString();
    }



    /**
     * Reads a validated integer in [lo, hi] using do-while.
     * Keeps prompting until a valid number is entered.
     */
    public int grabNumber(String prompt, int lo, int hi) {
        int num;
        do {
            emitInline(prompt);
            num = parseOrDefault(kbd.nextLine().trim(), lo - 1);
            if (num < lo || num > hi)
                emit("  !! " + AUTHOR + " says : please enter a number from "
                        + lo + " to " + hi + ". Try again.");
        } while (num < lo || num > hi);
        return num;
    }

    public int parseOrDefault(String raw, int defaultVal) {
        try   { return Integer.parseInt(raw); }
        catch (NumberFormatException ex) { return defaultVal; }
    }
}

