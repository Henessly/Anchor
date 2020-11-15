package dev.henessly.anchor;

import dev.henessly.anchor.listener.SleepListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Anchor extends JavaPlugin {

  @Override
  public void onEnable() {
    saveDefaultConfig();
    getServer().getPluginManager().registerEvents(new SleepListener(this), this);
  }
}
