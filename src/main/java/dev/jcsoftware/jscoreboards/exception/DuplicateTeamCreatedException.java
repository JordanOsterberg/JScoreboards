package dev.jcsoftware.jscoreboards.exception;

public class DuplicateTeamCreatedException extends JScoreboardException {

    public DuplicateTeamCreatedException(String name) {
        super("A team named " + name + " already exists on this scoreboard.");
    }

}
