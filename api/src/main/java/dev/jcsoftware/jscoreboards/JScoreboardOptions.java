package dev.jcsoftware.jscoreboards;

public class JScoreboardOptions {

  private JScoreboardTabHealthStyle tabHealthStyle;
  private boolean showHealthUnderName;

  public JScoreboardOptions(JScoreboardTabHealthStyle tabHealthStyle, boolean showHealthUnderName) {
    this.tabHealthStyle = tabHealthStyle;
    this.showHealthUnderName = showHealthUnderName;
  }

  public static JScoreboardOptions defaultOptions = new JScoreboardOptions(JScoreboardTabHealthStyle.NONE, false);

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
}
