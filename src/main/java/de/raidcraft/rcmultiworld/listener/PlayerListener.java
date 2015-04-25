package de.raidcraft.rcmultiworld.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Philip
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        RaidCraft.getComponent(RCMultiWorldPlugin.class).getPlayerManager().join(event.getPlayer());
        event.setJoinMessage(null);
        Bukkit.getScheduler().runTaskLater(RaidCraft.getComponent(RCMultiWorldPlugin.class), new DelayedChecker(event.getPlayer()), 10L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        // clear list if last player leave
        if (Bukkit.getOnlinePlayers().size() == 1) {
            RaidCraft.getComponent(RCMultiWorldPlugin.class).getPlayerManager().clear();
        }
        RaidCraft.getComponent(RCMultiWorldPlugin.class).getPlayerManager().leave(event.getPlayer());
        event.setQuitMessage(null);

    }

    public class DelayedChecker implements Runnable {

        private Player player;

        public DelayedChecker(Player player) {

            this.player = player;
        }

        @Override
        public void run() {

            RaidCraft.getComponent(RCMultiWorldPlugin.class).getTeleportRequestManager().teleportOnRequest(player);
        }
    }

}
