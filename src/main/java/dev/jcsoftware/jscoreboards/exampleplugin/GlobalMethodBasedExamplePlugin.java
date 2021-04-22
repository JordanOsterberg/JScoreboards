package dev.jcsoftware.jscoreboards.exampleplugin;

import dev.jcsoftware.jscoreboards.JGlobalMethodBasedScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboardOptions;
import dev.jcsoftware.jscoreboards.JScoreboardTabHealthStyle;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalMethodBasedExamplePlugin extends JavaPlugin {
  private JGlobalMethodBasedScoreboard scoreboard;
  private JScoreboardTeam taggedTeam;

  private int explosionTimer = 30;

  @Override
  public void onEnable() {
    super.onEnable();

    this.scoreboard = new JGlobalMethodBasedScoreboard(
        new JScoreboardOptions(JScoreboardTabHealthStyle.NONE, true)
    );

    this.scoreboard.setTitle("&c&lTNT&f&lTag");

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

      this.scoreboard.setLines("&eExplosion in &f" + explosionTimer);
    }, 0, 20);
  }

  @Override
  public void onDisable() {
    super.onDisable();

    scoreboard.destroy();

    getServer().getScheduler().cancelTasks(this);
  }
}