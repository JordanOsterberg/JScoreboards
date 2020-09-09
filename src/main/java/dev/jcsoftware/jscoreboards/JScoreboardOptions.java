package dev.jcsoftware.jscoreboards;

public class JScoreboardOptions {

    private String scoreboardTitle;
    private JScoreboardTabHealthStyle tabHealthStyle;
    private boolean showHealthUnderName;

    public JScoreboardOptions(String scoreboardTitle, JScoreboardTabHealthStyle tabHealthStyle, boolean showHealthUnderName) {
        this.scoreboardTitle = scoreboardTitle;
        this.tabHealthStyle = tabHealthStyle;
        this.showHealthUnderName = showHealthUnderName;
    }

    public static JScoreboardOptions defaultOptions = new JScoreboardOptions("&f&lMy Server", JScoreboardTabHealthStyle.NONE, false);

    public String getScoreboardTitle() {
        return scoreboardTitle;
    }

    public JScoreboardTabHealthStyle getTabHealthStyle() {
        return tabHealthStyle;
    }

    public boolean shouldShowHealthUnderName() {
        return showHealthUnderName;
    }

    /**
     * The scoreboard must be updated for this change to take effect.
     * @param showHealthUnderName
     */
    public void setShowHealthUnderName(boolean showHealthUnderName) {
        this.showHealthUnderName = showHealthUnderName;
    }

    /**
     * The scoreboard must be updated for this change to take effect.
     * @param tabHealthStyle
     */
    public void setTabHealthStyle(JScoreboardTabHealthStyle tabHealthStyle) {
        this.tabHealthStyle = tabHealthStyle;
    }

    /**
     * The scoreboard must be updated for this change to take effect.
     * @param scoreboardTitle
     */
    public void setScoreboardTitle(String scoreboardTitle) {
        this.scoreboardTitle = scoreboardTitle;
    }

}
