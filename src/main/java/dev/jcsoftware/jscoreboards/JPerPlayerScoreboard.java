package dev.jcsoftware.jscoreboards;

import dev.jcsoftware.jscoreboards.exception.JScoreboardException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.function.Function;

public class JPerPlayerScoreboard extends JScoreboard {

    private final Function<Player, List<String>> generateLinesFunction;
    private final Map<Player, Scoreboard> playerScoreboardMap = new HashMap<>();

    public JPerPlayerScoreboard(Function<Player, List<String>> generateLinesFunction, JScoreboardOptions options) {
        super(options);

        this.generateLinesFunction = generateLinesFunction;
    }

    /**
     * Call this when the contents of the scoreboard should change in some way.
     * @throws JScoreboardException
     */
    public void updateScoreboard() throws JScoreboardException {
        for (UUID playerUUID : activePlayers) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) continue;

            List<String> lines = this.generateLinesFunction.apply(player);
            Collections.reverse(lines);
            updateScoreboard(player.getScoreboard(), lines);
        }
    }

    /**
     * Add a player to the scoreboard.
     * @param player The player to add
     */
    @Override
    public void addPlayer(Player player) {
        activePlayers.add(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
        playerScoreboardMap.put(player, scoreboard);

        try {
            updateScoreboard();
        } catch (JScoreboardException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePlayer(Player player) {
        super.removePlayer(player);

        playerScoreboardMap.remove(player);
    }

    public Scoreboard toBukkitScoreboard(Player player) {
        return playerScoreboardMap.get(player);
    }

}
