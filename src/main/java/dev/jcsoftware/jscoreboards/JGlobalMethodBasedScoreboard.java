package dev.jcsoftware.jscoreboards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class JGlobalMethodBasedScoreboard extends JGlobalScoreboard {
  private String title = "";
  private List<String> lines = new ArrayList<>();

  public JGlobalMethodBasedScoreboard(JScoreboardOptions options) {
    super(options);

    setTitleSupplier(() -> title);
    setLinesSupplier(() -> lines);
  }

  public JGlobalMethodBasedScoreboard() {
    this(JScoreboardOptions.defaultOptions);
  }

  public void setTitle(String title) {
    this.title = title;
    updateScoreboard();
  }

  public void setLines(List<String> lines) {
    this.lines = lines;
    updateScoreboard();
  }

  public void setLines(String... lines) {
    setLines(Arrays.asList(lines));
  }
}
