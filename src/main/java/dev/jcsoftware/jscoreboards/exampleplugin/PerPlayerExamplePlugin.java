package dev.jcsoftware.jscoreboards.exampleplugin;

import dev.jcsoftware.jscoreboards.*;
import dev.jcsoftware.jscoreboards.exception.JScoreboardException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class PerPlayerExamplePlugin extends JavaPlugin {

    private JPerPlayerScoreboard scoreboard;
    private JScoreboardTeam taggedTeam;

    private int explosionTimer = 30;

    @Override
    public void onEnable() {
        super.onEnable();

        this.scoreboard = new JPerPlayerScoreboard(player -> {
            if (explosionTimer % 2 == 0) {
                return Arrays.asList(
                        "&aX: &7" + player.getLocation().getBlockX(),
                        "&aY: &7" + player.getLocation().getBlockY(),
                        "&aZ: &7" + player.getLocation().getBlockZ(),
                        "",
                        "&7Explosion in &c" + explosionTimer + "...",
                        "",
                        "No Remainder!"
                );
            } else {
                return Arrays.asList(
                        "&aX: &7" + player.getLocation().getBlockX(),
                        "&aY: &7" + player.getLocation().getBlockY(),
                        "&aZ: &7" + player.getLocation().getBlockZ(),
                        "",
                        "&7Explosion in &c" + explosionTimer + "..."
                );
            }
        }, new JScoreboardOptions("&4&lTNT&f&lTag", JScoreboardTabHealthStyle.NONE, false));

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
                this.scoreboard.updateScoreboard();
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
