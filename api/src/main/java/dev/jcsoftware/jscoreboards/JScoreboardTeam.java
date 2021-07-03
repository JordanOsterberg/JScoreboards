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

    for (UUID entityUUID : entities) {
      Player player = Bukkit.getPlayer(entityUUID);

      if (player != null && !team.hasEntry(player.getName())) {
        team.addEntry(player.getName());
      } else if (player == null) {
        if (!team.hasEntry(entityUUID.toString())) {
          team.addEntry(entityUUID.toString());
        }
      }
    }

    getScoreboard().getTeamWrapper().setColor(team, teamColor);
    team.setPrefix(ChatColor.translateAlternateColorCodes('&', getDisplayName()));
  }

  private void handleRemoval(String entry) {
    if (scoreboard instanceof JGlobalScoreboard) {
      Team team = toBukkitTeam(((JGlobalScoreboard) scoreboard).toBukkitScoreboard());
      if (team == null) return;
      team.removeEntry(entry);
      return;
    }

    JPerPlayerScoreboard perPlayerScoreboard = (JPerPlayerScoreboard) scoreboard;
    for (UUID scoreboardPlayer : perPlayerScoreboard.getActivePlayers()) {
      Player player = Bukkit.getPlayer(scoreboardPlayer);
      if (player == null) continue;
      Scoreboard playerScoreboard = perPlayerScoreboard.toBukkitScoreboard(player);

      Team team = toBukkitTeam(playerScoreboard);
      if (team == null) continue;
      team.removeEntry(entry);
    }
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
    if (!isOnTeam(player.getUniqueId())) return;

    removeEntity(player.getUniqueId());
    handleRemoval(player.getName());
  }

  public void removeEntity(Entity entity) {
    if (!isOnTeam(entity.getUniqueId())) return;

    removeEntity(entity.getUniqueId());
    handleRemoval(entity.getUniqueId().toString());
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
