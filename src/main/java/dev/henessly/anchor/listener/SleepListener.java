package dev.henessly.anchor.listener;

import dev.henessly.anchor.Anchor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.HashSet;
import java.util.Set;

public final class SleepListener implements Listener {

  private final Anchor anchor;

  private final int divisor;
  private final Set<Player> playersSleeping;

  public SleepListener(final Anchor anchor) {
    this.anchor = anchor;
    this.divisor = anchor.getConfig().getInt("divisor");

    this.playersSleeping = new HashSet<>();
  }

  @EventHandler
  public final void onSleepEnter(final PlayerBedEnterEvent event) {
    final Player player = event.getPlayer();
    if (PlayerBedEnterEvent.BedEnterResult.OK.equals(event.getBedEnterResult())) {
      playersSleeping.add(player);
      final Server server = anchor.getServer();
      final int playersNeeded = server.getOnlinePlayers().size() / divisor;
      final int sleepingSize = playersSleeping.size();
      if (sleepingSize >= playersNeeded) {
        server.broadcastMessage(ChatColor.GREEN + "Skipping to day!");
        server.getScheduler().runTask(anchor, () -> {
          final World  world = anchor.getServer().getWorld(player.getWorld().getUID());
          if (world != null) {
            world.setTime(1000L);
            world.setStorm(false);
            world.setThundering(false);
          }
        });
      } else {
        server.broadcastMessage(ChatColor.YELLOW + "" + playersNeeded + ChatColor.GREEN + " player(s) needed to skip night!");
      }
    }
  }

  @EventHandler
  public final void onSleepLeave(final PlayerBedLeaveEvent event) {
    playersSleeping.remove(event.getPlayer()); // Removes player from set
  }
}
