package dev.jcsoftware.jscoreboards.exampleplugin;

import dev.jcsoftware.jscoreboards.JScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboardOptions;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import dev.jcsoftware.jscoreboards.JScoreboardTabHealthStyle;
import dev.jcsoftware.jscoreboards.exception.JScoreboardException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TNTTagExamplePlugin extends JavaPlugin {

    private JScoreboard scoreboard;
    private JScoreboardTeam taggedTeam;

    private int explosionTimer = 30;

    @Override
    public void onEnable() {
        super.onEnable();

        this.scoreboard = new JScoreboard(
                new JScoreboardOptions("&4&lTNT&f&lTag", JScoreboardTabHealthStyle.NONE, false)
        );

        try {
            this.taggedTeam = this.scoreboard.createTeam("Tagged", "&c&lIT ");
        } catch (JScoreboardException e) {
            e.printStackTrace();
        }

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            explosionTimer--;
            if (explosionTimer < 0) {
                explosionTimer = 30;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                this.scoreboard.addPlayer(player);

                if (explosionTimer % 2 == 0) {
                    this.taggedTeam.addPlayer(player);
                } else {
                    this.taggedTeam.removePlayer(player);
                }
            }

            try {
                this.scoreboard.setLines("&eExplosion in &f" + explosionTimer);
            } catch (JScoreboardException e) {
                e.printStackTrace();
            }
        }, 0, 20);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        scoreboard.destroy();

        getServer().getScheduler().cancelTasks(this);
    }

}
