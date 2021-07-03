package dev.jcsoftware.jscoreboards.exception;

public class ScoreboardLineTooLongException extends RuntimeException {
  public ScoreboardLineTooLongException(String line, int maxLength) {
    super("Tried to register a scoreboard line with a length greater than " + maxLength + " characters.\nLine content:\"" + line + "\"");
  }
}
