package de.raidcraft.rcmultiworld.bungeecord;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author Philip
 */
public class BungeeListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String messageChannel, Player player, byte[] encoded) {

        if (!messageChannel.equals("BungeeCord")) return;
        RCMultiWorldPlugin plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
        BungeeManager bungeeManager = plugin.getBungeeManager();

        // custom channel message
        String message = BungeeCordUtil.decodeMessage(encoded, bungeeManager.getBungeeChannel());
        if (message == null) return;

        bungeeManager.receiveMessage(message);
    }
}
