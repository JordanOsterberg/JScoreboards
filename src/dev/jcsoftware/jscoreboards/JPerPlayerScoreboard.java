package dev.jcsoftware.jscoreboards;

import dev.jcsoftware.jscoreboards.exception.JScoreboardException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class JPerPlayerScoreboard extends JScoreboard {

    private Function<Player, List<String>> generateLinesFunction;

    public JPerPlayerScoreboard(Function<Player, List<String>> generateLinesFunction, JScoreboardOptions options) {
        super(options);

        this.generateLinesFunction = generateLinesFunction;
    }

    public void updateScoreboard() throws JScoreboardException {
        for (UUID playerUUID : activePlayers) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) continue;

            List<String> lines = this.generateLinesFunction.apply(player);
            Collections.reverse(lines);
            updateScoreboard(player.getScoreboard(), lines);
        }
    }

    @Override
    public void addPlayer(Player player) {
        activePlayers.add(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        try {
            updateScoreboard();
        } catch (JScoreboardException e) {
            e.printStackTrace();
        }
    }

}
