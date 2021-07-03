package dev.jcsoftware.jscoreboards.abstraction;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * The InternalObjectiveWrapper defines the requirements for version specific Spigot API communication with Scoreboard Objectives
 * See the top level README for more information.
 *
 * Don't implement this class yourself ;)
 */
public abstract class InternalObjectiveWrapper {
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
}