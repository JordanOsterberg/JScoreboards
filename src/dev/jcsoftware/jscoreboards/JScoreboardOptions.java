package dev.jcsoftware.jscoreboards;

public class JScoreboardOptions {

    private String scoreboardTitle;
    private boolean addHealthToTab;

    public JScoreboardOptions(String scoreboardTitle, boolean addHealthToTab) {
        this.scoreboardTitle = scoreboardTitle;
        this.addHealthToTab = addHealthToTab;
    }

    public static JScoreboardOptions defaultOptions = new JScoreboardOptions("&f&lMy Server", false);

    public String getScoreboardTitle() {
        return scoreboardTitle;
    }

    public boolean shouldAddHealthToTab() {
        return addHealthToTab;
    }

}
