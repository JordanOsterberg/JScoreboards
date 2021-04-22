package dev.jcsoftware.jscoreboards.exampleplugin;

import dev.jcsoftware.jscoreboards.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PerPlayerMethodBasedExamplePlugin extends JavaPlugin {
  private JPerPlayerMethodBasedScoreboard scoreboard;
  private JScoreboardTeam taggedTeam;

  private int explosionTimer = 30;

  @Override
  public void onEnable() {
    super.onEnable();

    this.scoreboard = new JPerPlayerMethodBasedScoreboard(
        new JScoreboardOptions(JScoreboardTabHealthStyle.NONE, true)
    );

    this.taggedTeam = this.scoreboard.createTeam("Tagged", "&c&lIT ");

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

        List<String> lines = new ArrayList<>();
        lines.add("&aX: &7" + player.getLocation().getBlockX());
        lines.add("&aY: &7" + player.getLocation().getBlockY());
        lines.add("&aZ: &7" + player.getLocation().getBlockZ());
        lines.add("");
        lines.add("&7Explosion in &c" + explosionTimer + "...");

        if (explosionTimer % 2 == 0) {
          lines.add("");
          lines.add("No Remainder!");
        }
        scoreboard.setLines(player, lines);
        scoreboard.setTitle(player, "&4&lTNT&f&lTag");
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