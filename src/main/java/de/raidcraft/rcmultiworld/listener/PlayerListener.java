package de.raidcraft.rcmultiworld.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import org.bukkit.Bukkit;
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

        event.setJoinMessage(null);
        RaidCraft.getComponent(RCMultiWorldPlugin.class).getTeleportRequestManager().teleportOnRequest(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        // clear list if last player leave
        if(Bukkit.getOnlinePlayers().length == 1) {
            RaidCraft.getComponent(RCMultiWorldPlugin.class).getPlayerManager().clear();
        }

        event.setQuitMessage(null);
    }

}
