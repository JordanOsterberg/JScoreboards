package dev.jcsoftware.jscoreboards.versioning;

import dev.jcsoftware.jscoreboards.abstraction.JScoreboardWrapper;
import dev.jcsoftware.jscoreboards.version.Wrapper1_16;
import dev.jcsoftware.jscoreboards.version.Wrapper1_8;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class VersionMatcher {
  private final String serverVersion =
      Bukkit.getServer().getClass().getPackage().getName()
          .split("\\.")[3]
          .substring(1);

  private final List<Class<? extends JScoreboardWrapper>> versions = Arrays.asList(
      Wrapper1_8.class,
      Wrapper1_16.class
  );

  public JScoreboardWrapper match() {
    try {
      return versions.stream()
          .filter(version -> serverVersion.contains(version.getSimpleName().substring(7)))
          .findFirst()
          .orElseGet(() -> {
            Bukkit.getLogger().warning("Your version of Spigot (" + serverVersion + ") is not officially supported or tested by JScoreboards.");
            Bukkit.getLogger().warning("Proceed with caution, and report bugs at https://github.com/JordanOsterberg/JScoreboards. Thanks!");
            return Wrapper1_16.class;
          })
          .getDeclaredConstructor()
          .newInstance();
    } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
      throw new RuntimeException(ex);
    }
  }
}
