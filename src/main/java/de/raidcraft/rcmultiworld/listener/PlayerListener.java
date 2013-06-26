package de.raidcraft.rcmultiworld.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.utilclasses.Locatable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philip
 */
public class PlayerListener implements Listener {

    private static Map<String, Locatable> queuedLoginTeleports = new HashMap<>();

    public static void enqueuePlayer(String player, Locatable locatable) {
        queuedLoginTeleports.put(player.toLowerCase(), locatable);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Locatable locatable = queuedLoginTeleports.remove(event.getPlayer().getName().toLowerCase());
        if(locatable != null) {
            if(!event.getPlayer().getWorld().getName().equalsIgnoreCase(locatable.getLocation().getWorld().getName())) {
                event.getPlayer().teleport(locatable.getLocation());
                event.getPlayer().sendMessage(ChatColor.YELLOW + "Teleported.");
            }
        }
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
