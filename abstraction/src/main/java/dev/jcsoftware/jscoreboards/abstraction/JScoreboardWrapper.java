package dev.jcsoftware.jscoreboards.abstraction;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public abstract class JScoreboardWrapper {
  public abstract Objective getNameHealthObjective(Scoreboard scoreboard);
  public abstract Objective getTabHealthObjective(WrappedHealthStyle wrappedHealthStyle, Scoreboard scoreboard);
  public abstract Objective getDummyObjective(Scoreboard scoreboard);
  public abstract void setColor(Team team, ChatColor color);
}
