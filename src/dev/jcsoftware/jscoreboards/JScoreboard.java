package dev.jcsoftware.jscoreboards;

import dev.jcsoftware.jscoreboards.exception.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class JScoreboard {

    private JScoreboardOptions options;

    private List<JScoreboardPlayerTeam> teams = new ArrayList<>();
    private List<String> lines = new ArrayList<>();
    protected List<UUID> activePlayers = new ArrayList<>();

    private Scoreboard scoreboard;

    public JScoreboard(JScoreboardOptions options) {
        this.options = options;
    }

    public JScoreboard() {
        this.options = JScoreboardOptions.defaultOptions;
    }

    private void createBukkitScoreboardIfNull() {
        if (this.scoreboard == null) {
            ScoreboardManager scoreboardManager = Bukkit.getServer().getScoreboardManager();
            if (scoreboardManager == null) return;

            scoreboard = scoreboardManager.getNewScoreboard();

            for (UUID playerUUID : activePlayers) {
                Player player = Bukkit.getPlayer(playerUUID);

                if (player != null) {
                    player.setScoreboard(scoreboard);
                }
            }
        }
    }

    protected void updateScoreboard() throws JScoreboardException {
        createBukkitScoreboardIfNull();
        updateScoreboard(scoreboard, lines);
    }

    /**
     * Update the scoreboard for all players it is shown to.
     * @throws JScoreboardException If a String within the lines array is over 64 characters, this exception is thrown.
     */
    protected void updateScoreboard(Scoreboard scoreboard, List<String> lines) throws JScoreboardException {
        Objective objective;

        if (scoreboard.getObjective("dummy") == null) {
            objective = scoreboard.registerNewObjective("dummy", "dummy", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(color(options.getScoreboardTitle()));

            if (options.shouldAddHealthToTab()) {
                Objective healthObjective = scoreboard.registerNewObjective("health", "health", "health", RenderType.HEARTS);
                healthObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            } else {
                Objective healthObjective = scoreboard.getObjective("health");
                if (healthObjective != null) {
                    healthObjective.unregister();
                }
            }

            List<String> colorCodeOptions = new ArrayList<>();
            for (ChatColor color : ChatColor.values()) {
                if (color.isFormat()) {
                    continue;
                }

                for (ChatColor secondColor : ChatColor.values()) {
                    if (secondColor.isFormat()) {
                        continue;
                    }

                    if (color != secondColor) {
                        colorCodeOptions.add(color + " " + secondColor);
                        if (colorCodeOptions.size() == lines.size()) break;
                    }
                }
            }

            int score = 1;
            for (String entry : lines) {
                if (score > 64) {
                    throw new ScoreboardLineTooLongException();
                }

                Team team = scoreboard.registerNewTeam("line" + score);
                team.addEntry(colorCodeOptions.get(score));
                team.setPrefix(color(entry));
                objective.getScore(colorCodeOptions.get(score)).setScore(score);

                score += 1;
            }
        } else {
            int score = 1;

            for (String entry : lines) {
                Team team = scoreboard.getTeam("line" + score);

                if (team != null) {
                    team.setPrefix(color(entry));
                }

                score += 1;
            }
        }

        for (JScoreboardPlayerTeam team : this.teams) {
            team.refresh();
        }
    }

    public void setLines(List<String> lines) throws JScoreboardException {
        Collections.reverse(lines);
        this.lines = lines;
        updateScoreboard();
    }

    public void setLines(String... lines) throws JScoreboardException {
        List<String> linesList = new ArrayList<>();
        for (String line : lines) {
            linesList.add(line);
        }
        setLines(linesList);
    }

    /**
     * Add a player to the scoreboard
     * @param player The player to add
     */
    public void addPlayer(Player player) {
        this.activePlayers.add(player.getUniqueId());

        createBukkitScoreboardIfNull();
        player.setScoreboard(scoreboard);
    }

    /**
     * Remove the player from the JScoreboard. This will reset their scoreboard to the main scoreboard
     * @param player The player to remove
     */
    public void removePlayer(Player player) {
        this.activePlayers.remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    /**
     * Find a team using a name
     * @param name The name to search for. Color codes will be stripped from both the team name and this variable.
     * @return The JScoreboardPlayerTeam found, if any. Will return null if no team exists
     */
    public JScoreboardPlayerTeam findTeam(String name) {
        for (JScoreboardPlayerTeam team : teams) {
            if (ChatColor.stripColor(team.getName()).equalsIgnoreCase(ChatColor.stripColor(name))) {
                return team;
            }
        }

        return null;
    }

    /**
     * Create a team using a name. You can use color codes to color the name in tab and above player heads.
     * @param name The name for the new team. This name cannot be longer than 16 characters
     * @return The created JScoreboardPlayerTeam
     * @throws DuplicateTeamCreatedException If a team with that name already exists
     */
    public JScoreboardPlayerTeam createTeam(String name, String displayName) throws JScoreboardException {
        for (JScoreboardPlayerTeam team : this.teams) {
            if (ChatColor.stripColor(team.getName()).equalsIgnoreCase(ChatColor.stripColor(name))) {
                throw new DuplicateTeamCreatedException(name);
            }
        }

        if (name.length() > 16) {
            throw new ScoreboardTeamNameTooLongException(name);
        }

        createBukkitScoreboardIfNull();

        JScoreboardPlayerTeam team = new JScoreboardPlayerTeam(name, displayName, this);
        team.refresh();
        this.teams.add(team);
        return team;
    }

    /**
     * Remove a team from the scoreboard
     * @param team The team to remove from the scoreboard
     */
    public void removeTeam(JScoreboardPlayerTeam team) {
        if (team.getScoreboard() != this) return;

        team.destroy();
        this.teams.remove(team);
    }

    /**
     * Destroy the scoreboard. This will reset all players to the server's main scoreboard
     * You should call this method inside of your JavaPlugin's onDisable method.
     */
    public void destroy() {
        for (UUID playerUUID : activePlayers) {
            Player player = Bukkit.getPlayer(playerUUID);

            if (player != null) {
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
        }

        for (JScoreboardPlayerTeam team : teams) {
            team.destroy();
        }

        this.activePlayers.clear();
        this.lines.clear();
        this.teams.clear();
        this.scoreboard = null;
    }

    public Scoreboard toBukkitScoreboard() {
        return scoreboard;
    }

    private String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
