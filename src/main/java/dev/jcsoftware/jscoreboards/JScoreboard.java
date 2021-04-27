package dev.jcsoftware.jscoreboards;

import dev.jcsoftware.jscoreboards.exception.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public abstract class JScoreboard {
  private JScoreboardOptions options;

  private final List<JScoreboardTeam> teams = new ArrayList<>();
  protected List<UUID> activePlayers = new ArrayList<>();

  private final Map<Scoreboard, List<String>> previousLinesMap = new HashMap<>();

  // MARK: Public API

  /**
   * Add a player to the scoreboard
   * @param player The player to add
   */
  public void addPlayer(Player player) {
    this.activePlayers.add(player.getUniqueId());
  }

  /**
   * Remove the player from the JScoreboard, and remove any teams they may be a member of.
   * This will reset their scoreboard to the main scoreboard.
   * @param player The player to remove
   */
  public void removePlayer(Player player) {
    this.activePlayers.remove(player.getUniqueId());
    Validate.notNull(Bukkit.getScoreboardManager());
    player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

    teams.forEach(team -> {
      if (team.isOnTeam(player.getUniqueId())) {
        team.removePlayer(player);
      }
    });
  }

  /**
   * Find a team using a name
   * @param name The name to search for. Color codes will be stripped from both the team name and this variable.
   * @return The JScoreboardPlayerTeam found, if any. Will return null if no team exists
   */
  public Optional<JScoreboardTeam> findTeam(String name) {
    return teams.stream()
        .filter(team -> ChatColor.stripColor(team.getName()).equalsIgnoreCase(ChatColor.stripColor(name)))
        .findAny();
  }

  /**
   * Create a team on the scoreboard. ChatColor.WHITE is used as the color for the team.
   * @param name The name for the new team. This name cannot be longer than 16 characters
   * @return The created JScoreboardPlayerTeam
   * @throws DuplicateTeamCreatedException If a team with that name already exists
   * @throws ScoreboardTeamNameTooLongException If the team's name is longer than 16 characters
   */
  public JScoreboardTeam createTeam(String name, String displayName) throws DuplicateTeamCreatedException, ScoreboardTeamNameTooLongException {
    return createTeam(name, displayName, ChatColor.WHITE);
  }

  /**
   * Create a team on the scoreboard.
   * @param name The name for the new team. This name cannot be longer than 16 characters
   * @return The created JScoreboardPlayerTeam
   * @throws DuplicateTeamCreatedException If a team with that name already exists
   * @throws ScoreboardTeamNameTooLongException If the team's name is longer than 16 characters
   */
  public JScoreboardTeam createTeam(String name, String displayName, ChatColor teamColor) throws DuplicateTeamCreatedException, ScoreboardTeamNameTooLongException {
    for (JScoreboardTeam team : this.teams) {
      if (ChatColor.stripColor(team.getName()).equalsIgnoreCase(ChatColor.stripColor(name))) {
        throw new DuplicateTeamCreatedException(name);
      }
    }

    if (name.length() > 16) {
      throw new ScoreboardTeamNameTooLongException(name);
    }

    JScoreboardTeam team = new JScoreboardTeam(name, displayName, teamColor,this);
    team.refresh();
    this.teams.add(team);
    return team;
  }

  /**
   * Remove a team from the scoreboard
   * @param team The team to remove from the scoreboard
   */
  public void removeTeam(JScoreboardTeam team) {
    if (team.getScoreboard() != this) return;

    team.destroy();
    this.teams.remove(team);
  }

  /**
   * Destroy the scoreboard. This will reset all players to the server's main scoreboard, and clear all teams.
   * You should call this method inside of your plugin's onDisable method.
   */
  public void destroy() {
    for (UUID playerUUID : activePlayers) {
      Player player = Bukkit.getPlayer(playerUUID);

      if (player != null) {
        Validate.notNull(Bukkit.getScoreboardManager());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
      }
    }

    for (JScoreboardTeam team : teams) {
      team.destroy();
    }

    this.activePlayers.clear();
    this.teams.clear();
  }

  /**
   * Get the teams registered on the Scoreboard
   * @return The teams registered on the scoreboard
   */
  public List<JScoreboardTeam> getTeams() {
    return teams;
  }

  /**
   * Get the options for the Scoreboard.
   * If changed directly, updateScoreboard() must be called manually.
   * @return The options for the scoreboard
   */
  public JScoreboardOptions getOptions() {
    return options;
  }

  // MARK: Private API

  /**
   * Update a scoreboard with a list of lines
   * @throws ScoreboardLineTooLongException If a String within the lines array is over 64 characters, this exception is thrown.
   */
  protected void updateScoreboard(Scoreboard scoreboard, List<String> lines) throws ScoreboardLineTooLongException {
    Objective objective;

    if (scoreboard.getObjective("dummy") == null) {
      objective = scoreboard.registerNewObjective("dummy", "dummy", "dummy");
    } else {
      objective = scoreboard.getObjective("dummy");
    }

    Validate.notNull(objective);

    objective.setDisplayName(color(getTitle(scoreboard)));

    if (previousLinesMap.containsKey(scoreboard)) {
      if (previousLinesMap.get(scoreboard).equals(lines)) { // Are the lines the same? Don't take up server resources to change absolutely nothing
        updateTeams(scoreboard); // Update the teams anyway
        return;
      }

      // Size difference means unregister objective to reset and re-register teams correctly
      if (previousLinesMap.get(scoreboard).size() != lines.size()) {
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        scoreboard.getEntries().forEach(scoreboard::resetScores);
        scoreboard.getTeams().forEach(team -> {
          if (team.getName().contains("line")) {
            team.unregister();
          }
        });
      }
    }

    previousLinesMap.put(scoreboard, lines);

    List<String> reversedLines = new ArrayList<>(lines);
    Collections.reverse(reversedLines);

    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    Objective healthObjective = scoreboard.getObjective("tabHealth");
    if (options.getTabHealthStyle() != JScoreboardTabHealthStyle.NONE) {
      if (healthObjective == null) {
        healthObjective = scoreboard.registerNewObjective(
            "tabHealth",
            "health",
            "health",
            options.getTabHealthStyle() == JScoreboardTabHealthStyle.HEARTS ? RenderType.HEARTS : RenderType.INTEGER
        );
      }

      healthObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    } else {
      if (healthObjective != null) {
        healthObjective.unregister();
      }
    }

    if (options.shouldShowHealthUnderName()) {
      healthObjective = scoreboard.getObjective("nameHealth");
      if (healthObjective == null) {
        healthObjective = scoreboard.registerNewObjective(
            "nameHealth",
            "health",
            ChatColor.translateAlternateColorCodes('&', "&c❤")
        );
      }

      healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
    } else {
      healthObjective = scoreboard.getObjective("nameHealth");
      if (healthObjective != null) {
        healthObjective.unregister();
      }
    }

    List<String> colorCodeOptions = colorOptions(reversedLines.size());

    int score = 1;

    for (String entry : reversedLines) {
      if (entry.length() > 64) {
        throw new ScoreboardLineTooLongException(entry);
      }

      Team team = scoreboard.getTeam("line" + score);

      if (team != null) {
        team.setPrefix(color(entry));
        team.getEntries().forEach(team::removeEntry);
        team.addEntry(colorCodeOptions.get(score));
      } else {
        team = scoreboard.registerNewTeam("line" + score);
        team.addEntry(colorCodeOptions.get(score));
        team.setPrefix(color(entry));
        objective.getScore(colorCodeOptions.get(score)).setScore(score);
      }

      score += 1;
    }

    updateTeams(scoreboard);
  }

  /**
   * Update the teams on the scoreboard. Loops over all teams and calls refresh(Scoreboard)
   * @param scoreboard The Bukkit scoreboard to use
   */
  private void updateTeams(Scoreboard scoreboard) {
    this.teams.forEach(team -> team.refresh(scoreboard));
  }

  /**
   * Generate a list of unique color code combinations to use as scoreboard entries.
   * This is done to ensure that...
   * 1. Duplicate lines can be created
   * 2. The content of a scoreboard line is stored in the team prefix + suffix, rather than the entry itself
   * @param amountOfLines The amount of lines, and by proxy the amount of color combinations, to be generated
   * @return The list of unique color combinations
   */
  private List<String> colorOptions(int amountOfLines) {
    List<String> colorCodeOptions = new ArrayList<>();
    for (ChatColor color : ChatColor.values()) {
      if (color.isFormat()) {
        continue;
      }

      for (ChatColor secondColor : ChatColor.values()) {
        if (secondColor.isFormat()) {
          continue;
        }

        String option = color + " " + secondColor;

        if (color != secondColor && !colorCodeOptions.contains(option)) {
          colorCodeOptions.add(option);

          if (colorCodeOptions.size() == amountOfLines) break;
        }
      }
    }

    return colorCodeOptions;
  }

  /**
   * Set the options of the scoreboard
   * @param options The options
   */
  protected void setOptions(JScoreboardOptions options) {
    this.options = options;
  }

  /**
   * Translate a String into a color formatted String. Uses & as the special color char.
   * @param string The string to translate
   * @return The formatted String
   */
  protected String color(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  /**
   * Get the title for a particular Scoreboard
   * @param scoreboard The Bukkit scoreboard that requires a title
   * @return The title String
   */
  protected abstract String getTitle(Scoreboard scoreboard);
}
