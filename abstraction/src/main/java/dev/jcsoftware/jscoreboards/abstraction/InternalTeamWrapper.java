package dev.jcsoftware.jscoreboards.abstraction;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

/**
 * The InternalTeamWrapper defines the requirements for version specific Spigot API communication with Scoreboard Teams
 * See the top level README for more information.
 *
 * Don't implement this class yourself ;)
 */
public abstract class InternalTeamWrapper {
  /**
   * Set the Color for a Scoreboard Team
   * @param team The Bukkit Team to set the color on
   * @param color The ChatColor to use for coloring
   */
  public abstract void setColor(Team team, ChatColor color);
}
