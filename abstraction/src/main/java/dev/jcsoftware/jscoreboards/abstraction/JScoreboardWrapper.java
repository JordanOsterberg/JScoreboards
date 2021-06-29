package dev.jcsoftware.jscoreboards.abstraction;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public abstract class JScoreboardWrapper {
  /**
   * Get the name health objective from this Scoreboard instance
   *
   * If one doesn't exist, it will register a new one.
   * @param scoreboard The Bukkit Scoreboard to get the objective for
   * @return The Objective, either freshly registered or old
   */
  public abstract Objective getNameHealthObjective(Scoreboard scoreboard);

  /**
   * Get the tab health objective from this Scoreboard instance
   *
   * If one doesn't exist, it will register a new one.
   * @param scoreboard The Bukkit Scoreboard to get the objective for
   * @return The Objective, either freshly registered or old
   */
  public abstract Objective getTabHealthObjective(WrappedHealthStyle wrappedHealthStyle, Scoreboard scoreboard);

  /**
   * Get the dummy objective from this Scoreboard instance
   *
   * If one doesn't exist, it will register a new one.
   * @param scoreboard The Bukkit Scoreboard to get the objective for
   * @return The Objective, either freshly registered or old
   */
  public abstract Objective getDummyObjective(Scoreboard scoreboard);

  /**
   * Set the Color for a Scoreboard Team
   * @param team The Bukkit Team to set the color on
   * @param color The ChatColor to use for coloring
   */
  public abstract void setColor(Team team, ChatColor color);
}