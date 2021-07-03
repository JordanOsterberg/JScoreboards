package dev.jcsoftware.jscoreboards.version;

import dev.jcsoftware.jscoreboards.abstraction.InternalTeamWrapper;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class TeamWrapper_v1_12 extends InternalTeamWrapper {
  @Override
  public void setColor(Team team, ChatColor color) {
    team.setColor(color); // "All that for a drop of blood" - Thanos
  }
}
