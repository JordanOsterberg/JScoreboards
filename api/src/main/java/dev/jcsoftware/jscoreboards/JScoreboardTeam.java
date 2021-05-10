package dev.jcsoftware.jscoreboards;

import dev.jcsoftware.jscoreboards.exception.ScoreboardTeamNameTooLongException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JScoreboardTeam {
  private String name;
  private String displayName;
  private final ChatColor teamColor;
  private final List<UUID> entities = new ArrayList<>();
  private final JScoreboard scoreboard;

  protected JScoreboardTeam(String name, String displayName, ChatColor teamColor, JScoreboard scoreboard) {
    this.name = name;
    this.displayName = displayName;
    this.teamColor = teamColor;
    this.scoreboard = scoreboard;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name.length() > 16) {
      throw new ScoreboardTeamNameTooLongException(name);
    }

    this.name = name;
    refresh();
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
    refresh();
  }

  public void refresh() {
    if (this.scoreboard instanceof JPerPlayerScoreboard) {
      for (UUID uuid : this.entities) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
          refresh(player.getScoreboard());
        }
      }
    } else {
      refresh(((JGlobalScoreboard) this.scoreboard).toBukkitScoreboard());
    }
  }

  public void refresh(Scoreboard scoreboard) {
    Team team = toBukkitTeam(scoreboard);

    if (team == null) return;

    for (UUID playerUUID : entities) {
      Player player = Bukkit.getPlayer(playerUUID);

      if (player != null && !team.hasEntry(player.getName())) {
        team.addEntry(player.getName());
      }
    }

    getScoreboard().getWrapper().setColor(team, teamColor);
    team.setPrefix(ChatColor.translateAlternateColorCodes('&', getDisplayName()));
  }

  public Team toBukkitTeam(Scoreboard bukkitScoreboard) {
    if (bukkitScoreboard == null) return null;

    Team team;

    if (bukkitScoreboard.getTeam(name) != null) {
      team = bukkitScoreboard.getTeam(name);
    } else {
      team = bukkitScoreboard.registerNewTeam(name);
    }

    return team;
  }

  public void addPlayer(Player player) {
    addEntity(player.getUniqueId());
  }

  public void addEntity(Entity entity) {
    addEntity(entity.getUniqueId());
  }

  public void addEntity(UUID uuid) {
    if (entities.contains(uuid)) return;

    entities.add(uuid);

    refresh();
  }

  public void removePlayer(Player player) {
    removeEntity(player.getUniqueId());
  }

  public void removeEntity(Entity entity) {
    removeEntity(entity.getUniqueId());
  }

  public void removeEntity(UUID uuid) {
    entities.remove(uuid);

    refresh();
  }

  /**
   * Destroy the team. Will remove the team from the scoreboard and clear entries.
   */
  protected void destroy() {
    if (this.scoreboard instanceof JPerPlayerScoreboard) {
      for (UUID uuid : this.entities) {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
          Team team = player.getScoreboard().getTeam(name);
          if (team != null) team.unregister();
        }
      }
    } else {
      Scoreboard scoreboard = ((JGlobalScoreboard) this.scoreboard).toBukkitScoreboard();
      Team team = scoreboard.getTeam(name);
      if (team != null) team.unregister();
    }

    entities.clear();
  }

  public JScoreboard getScoreboard() {
    return scoreboard;
  }

  public List<UUID> getEntities() {
    return entities;
  }

  public boolean isOnTeam(UUID uuid) {
    return getEntities().contains(uuid);
  }
}
