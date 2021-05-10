package dev.jcsoftware.jscoreboards;

import dev.jcsoftware.jscoreboards.abstraction.WrappedHealthStyle;

public enum JScoreboardTabHealthStyle {
  NONE,
  HEARTS,
  NUMBER;

  public WrappedHealthStyle toWrapped() {
    switch (this) {
      case HEARTS: return WrappedHealthStyle.HEARTS;
      case NONE: return WrappedHealthStyle.NONE;
      case NUMBER: return WrappedHealthStyle.NUMBER;
    }

    return WrappedHealthStyle.NONE;
  }
}
