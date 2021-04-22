package dev.jcsoftware.jscoreboards.exception;

public class ScoreboardTeamNameTooLongException extends RuntimeException {
  public ScoreboardTeamNameTooLongException(String name) {
    super("Your scoreboard team name, \"" + name + "\", is longer than 16 characters.");
  }
}
