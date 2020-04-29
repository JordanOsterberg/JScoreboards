package dev.jcsoftware.jscoreboards.exception;

public class ScoreboardTeamNameTooLongException extends JScoreboardException {
    public ScoreboardTeamNameTooLongException(String name) {
        super("Your scoreboard team name, \"" + name + "\", is longer than 16 characters.");
    }
}
