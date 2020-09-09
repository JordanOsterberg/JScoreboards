package dev.jcsoftware.jscoreboards.exception;

public class ScoreboardManagerNullException extends JScoreboardException {
    public ScoreboardManagerNullException() {
        super("Bukkit's scoreboard manager is null. Please report this!");
    }
}
