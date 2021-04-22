package dev.jcsoftware.jscoreboards.exampleplugin;

import dev.jcsoftware.jscoreboards.*;
import dev.jcsoftware.jscoreboards.JGlobalScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class GlobalExamplePlugin extends JavaPlugin {
  private JGlobalScoreboard scoreboard;
  private JScoreboardTeam taggedTeam;

  private int explosionTimer = 30;

  @Override
  public void onEnable() {
    super.onEnable();

    this.scoreboard = new JGlobalScoreboard(
        () -> "&4&lTNT&f&lTag",
        () -> Collections.singletonList("&eExplosion in &f" + explosionTimer),
        new JScoreboardOptions(JScoreboardTabHealthStyle.NONE, true)
    );

    this.taggedTeam = this.scoreboard.createTeam("Tagged", "&c&lIT ", ChatColor.RED);

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

      this.scoreboard.updateScoreboard();
    }, 0, 20);
  }

  @Override
  public void onDisable() {
    super.onDisable();

    scoreboard.destroy();

    getServer().getScheduler().cancelTasks(this);
  }
}
