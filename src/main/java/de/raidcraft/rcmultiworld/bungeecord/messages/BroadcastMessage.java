package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.util.StringEncodingUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * @author Philip
 */
@MessageName("BROADCAST_MESSAGE")
public class BroadcastMessage extends BungeeMessage {

    private String message;
    private String server;
    private RCMultiWorldPlugin plugin;

    public BroadcastMessage(String server, String message) {

        this.message = message;
        this.server = server;
        plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    @Override
    protected String encode() {

        return server + BungeeManager.DELIMITER + StringEncodingUtil.encode(message);
    }

    @Override
    public void process() {

        for(World world : Bukkit.getWorlds()) {

            if(world.getName().equalsIgnoreCase(server)) {
                Bukkit.broadcastMessage(StringEncodingUtil.decode(message));
            }
        }
    }
}
