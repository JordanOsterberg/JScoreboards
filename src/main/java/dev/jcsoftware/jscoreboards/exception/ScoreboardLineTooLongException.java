package dev.jcsoftware.jscoreboards.exception;

public class ScoreboardLineTooLongException extends RuntimeException {
  public ScoreboardLineTooLongException(String line) {
    super("Tried to register a scoreboard line with a length greater than 64 characters.\nLine content:\"" + line + "\"");
  }
}
