package dev.jcsoftware.jscoreboards;

import dev.jcsoftware.jscoreboards.exception.ScoreboardLineTooLongException;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.function.Function;

public class JPerPlayerScoreboard extends JScoreboard {

  private Function<Player, String> generateTitleFunction;
  private Function<Player, List<String>> generateLinesFunction;
  private final Map<UUID, Scoreboard> playerScoreboardMap = new HashMap<>();

  public JPerPlayerScoreboard(JScoreboardOptions options) {
    setOptions(options);
  }

  public JPerPlayerScoreboard(
      Function<Player, String> generateTitleFunction,
      Function<Player, List<String>> generateLinesFunction,
      JScoreboardOptions options
  ) {
    setOptions(options);

    this.generateTitleFunction = generateTitleFunction;
    this.generateLinesFunction = generateLinesFunction;
  }

  public JPerPlayerScoreboard(
      Function<Player, String> generateTitleFunction,
      Function<Player, List<String>> generateLinesFunction
  ) {
    setOptions(JScoreboardOptions.defaultOptions);

    this.generateTitleFunction = generateTitleFunction;
    this.generateLinesFunction = generateLinesFunction;
  }

  // MARK: Public API

  /**
   * Call this when the contents of the scoreboard should change in some way.
   * @throws ScoreboardLineTooLongException Thrown if the scoreboard contains a line over 64 characters
   */
  public void updateScoreboard() throws ScoreboardLineTooLongException {
    if (generateLinesFunction == null) return; // Line generator is not ready yet

    for (UUID playerUUID : getActivePlayers()) {
      Player player = Bukkit.getPlayer(playerUUID);
      if (player == null) continue;

      List<String> lines = this.generateLinesFunction.apply(player);
      if (lines == null) {
        lines = new ArrayList<>();
      }
      updateScoreboard(player.getScoreboard(), lines);
    }
  }

  /**
   * Add a player to the scoreboard.
   * @param player The player to add
   */
  @Override
  public void addPlayer(Player player) {
    getActivePlayers().add(player.getUniqueId());

    Validate.notNull(Bukkit.getScoreboardManager());

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    player.setScoreboard(scoreboard);
    playerScoreboardMap.put(player.getUniqueId(), scoreboard);

    updateScoreboard();
  }

  /**
   * Remove a player from the scoreboard.
   * @param player The player to remove
   */
  @Override
  public void removePlayer(Player player) {
    super.removePlayer(player);

    playerScoreboardMap.remove(player.getUniqueId());
  }

  /**
   * Change the options of a scoreboard. Will update the scoreboard
   * @param options The new options for the scoreboard
   */
  @Override
  public void setOptions(JScoreboardOptions options) {
    super.setOptions(options);
    updateScoreboard();
  }

  /**
   * Get the Bukkit Scoreboard for a specific player
   * This will not change in between updates, but will change if the player is removed and re-added to the JScoreboard.
   * @param player The player to get the scoreboard for
   * @return The Bukkit Scoreboard
   */
  public Scoreboard toBukkitScoreboard(Player player) {
    return playerScoreboardMap.get(player.getUniqueId());
  }

  // MARK: Private API

  @Override
  protected String getTitle(Scoreboard scoreboard) {
    return generateTitleFunction.apply(
        playerForScoreboard(scoreboard)
    );
  }

  private Player playerForScoreboard(Scoreboard scoreboard) {
    if (!playerScoreboardMap.containsValue(scoreboard)) return null;
    Player player = null;
    for (Map.Entry<UUID, Scoreboard> entry : playerScoreboardMap.entrySet()) {
      if (entry.getValue().equals(scoreboard)) {
        player = Bukkit.getPlayer(entry.getKey());
        break;
      }
    }
    return player;
  }

  protected void setGenerateLinesFunction(Function<Player, List<String>> generateLinesFunction) {
    this.generateLinesFunction = generateLinesFunction;
  }

  protected void setGenerateTitleFunction(Function<Player, String> generateTitleFunction) {
    this.generateTitleFunction = generateTitleFunction;
  }
}
