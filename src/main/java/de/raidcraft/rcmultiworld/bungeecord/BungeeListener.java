package de.raidcraft.rcmultiworld.bungeecord;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.players.PlayerManager;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

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
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(encoded));
            String subchannel = in.readUTF();
            if (subchannel.equals("PlayerList")) {
                String server = in.readUTF();
                String list = in.readUTF();
                playerManager.updatePlayerList(list);
            }
        } catch (IOException e) {
        }

        // custom channel message
        String message = BungeeCordUtil.decodeMessage(encoded, bungeeManager.getBungeeChannel());
        if(message == null) return;

        bungeeManager.receiveMessage(message);
    }
}
