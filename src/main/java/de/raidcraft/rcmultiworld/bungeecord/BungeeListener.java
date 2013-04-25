package de.raidcraft.rcmultiworld.bungeecord;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.players.PlayerManager;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author Philip
 */
public class BungeeListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String messageChannel, Player player, byte[] encoded) {

        if(!messageChannel.equals("BungeeCord")) return;
        RCMultiWorldPlugin plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
        BungeeManager bungeeManager = plugin.getBungeeManager();
        PlayerManager playerManager = plugin.getPlayerManager();

        // update player list
        String message = BungeeCordUtil.decodeMessage(encoded, "PlayerList");
        if(message != null) {
            playerManager.updatePlayerList(message);
            return;
        }

        message = BungeeCordUtil.decodeMessage(encoded, bungeeManager.getBungeeChannel());
        if(message == null) return;

        bungeeManager.receiveMessage(message);
    }
}
