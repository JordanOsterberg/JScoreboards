package dev.jcsoftware.jscoreboards.exception;

public class ScoreboardLineTooLongException extends JScoreboardException {

    public ScoreboardLineTooLongException() {
        super("Tried to register a scoreboard line with a length greater than 64 characters");
    }

}
