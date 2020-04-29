package dev.jcsoftware.jscoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JScoreboardPlayerTeam {

    private String name;
    private String displayName;
    private List<UUID> players = new ArrayList<>();
    private JScoreboard scoreboard;

    protected JScoreboardPlayerTeam(String name, String displayName, JScoreboard scoreboard) {
        this.name = name;
        this.displayName = displayName;
        this.scoreboard = scoreboard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void refresh() {
        if (this.scoreboard instanceof JPerPlayerScoreboard) {
            for (UUID uuid : this.players) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    refresh(player.getScoreboard());
                }
            }
        } else {
            refresh(this.scoreboard.toBukkitScoreboard());
        }
    }

    private void refresh(Scoreboard scoreboard) {
        Team team;

        if (scoreboard.getTeam(name) != null) {
            team = scoreboard.getTeam(name);
        } else {
            team = scoreboard.registerNewTeam(name);
        }

        for (String entry : team.getEntries()) {
            team.removeEntry(entry);
        }

        for (UUID playerUUID : players) {
            Player player = Bukkit.getPlayer(playerUUID);

            if (player != null) {
                team.addEntry(player.getName());
            }
        }

        team.setPrefix(ChatColor.translateAlternateColorCodes('&', getDisplayName()));
    }

    public void addPlayer(Player player) {
        addPlayer(player.getUniqueId());
    }

    public void addPlayer(UUID uuid) {
        if (players.contains(uuid)) return;

        players.add(uuid);

        refresh();
    }

    public void removePlayer(Player player) {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);

        refresh();
    }

    protected void destroy() {
        if (this.scoreboard instanceof JPerPlayerScoreboard) {
            for (UUID uuid : this.players) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    player.getScoreboard().getTeam(name).unregister();
                }
            }
        } else {
            Scoreboard scoreboard = this.scoreboard.toBukkitScoreboard();
            scoreboard.getTeam(name).unregister();
        }

        for (UUID uuid : this.players) {
            removePlayer(uuid);
        }
    }

    public JScoreboard getScoreboard() {
        return scoreboard;
    }

}
