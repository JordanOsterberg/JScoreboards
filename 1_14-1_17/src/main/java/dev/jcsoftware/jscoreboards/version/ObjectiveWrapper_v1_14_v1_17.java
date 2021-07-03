package dev.jcsoftware.jscoreboards.version;

import dev.jcsoftware.jscoreboards.abstraction.InternalObjectiveWrapper;
import dev.jcsoftware.jscoreboards.abstraction.WrappedHealthStyle;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

public final class ObjectiveWrapper_v1_14_v1_17 extends InternalObjectiveWrapper {
  @Override
  public Objective getNameHealthObjective(Scoreboard scoreboard) {
    Objective healthObjective = scoreboard.getObjective("nameHealth");
    if (healthObjective == null) {
      healthObjective = scoreboard.registerNewObjective(
          "nameHealth",
          "health",
          ChatColor.translateAlternateColorCodes('&', "&c❤")
      );
    }
    return healthObjective;
  }

  @Override
  public Objective getTabHealthObjective(WrappedHealthStyle wrappedHealthStyle, Scoreboard scoreboard) {
    Objective healthObjective = scoreboard.getObjective("tabHealth");
    if (healthObjective == null) {
      healthObjective = scoreboard.registerNewObjective(
          "tabHealth",
          "health",
          "health",
          wrappedHealthStyle == WrappedHealthStyle.HEARTS ? RenderType.HEARTS : RenderType.INTEGER
      );
    }
    return healthObjective;
  }

  @Override
  public Objective getDummyObjective(Scoreboard scoreboard) {
    Objective objective = scoreboard.getObjective("dummy");
    if (objective == null) {
      objective = scoreboard.registerNewObjective("dummy", "dummy", "dummy");
    }
    return objective;
  }
}
