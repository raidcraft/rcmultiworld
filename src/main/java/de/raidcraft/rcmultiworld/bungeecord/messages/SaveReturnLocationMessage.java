package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.players.MultiWorldPlayer;
import de.raidcraft.rcmultiworld.utilclasses.ServerLocation;
import org.bukkit.Location;

import java.util.UUID;

/**
 * @author Philip
 */
@MessageName("SAVE_RETURN_LOCATION_MESSAGE")
public class SaveReturnLocationMessage extends BungeeMessage {

    private UUID player;
    private String x;
    private String y;
    private String z;
    private String yaw;
    private String pitch;
    private String world;
    private RCMultiWorldPlugin plugin;

    public SaveReturnLocationMessage(UUID player, String x, String y, String z, String yaw, String pitch, String world) {

        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    public SaveReturnLocationMessage(UUID player, Location location) {

        this.player = player;
        this.x = String.valueOf(location.getX());
        this.y = String.valueOf(location.getY());
        this.z = String.valueOf(location.getZ());
        this.yaw = String.valueOf(location.getYaw());
        this.pitch = String.valueOf(location.getPitch());
        this.world = location.getWorld().getName();
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    public SaveReturnLocationMessage(UUID player, ServerLocation serverLocation) {

        this.player = player;
        this.x = String.valueOf(serverLocation.getX());
        this.y = String.valueOf(serverLocation.getY());
        this.z = String.valueOf(serverLocation.getZ());
        this.yaw = String.valueOf(serverLocation.getYaw());
        this.pitch = String.valueOf(serverLocation.getPitch());
        this.world = serverLocation.getServer();
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    @Override
    protected String encode() {

        return player + BungeeManager.DELIMITER + x + BungeeManager.DELIMITER + y + BungeeManager.DELIMITER + z + BungeeManager.DELIMITER
                + yaw + BungeeManager.DELIMITER + pitch + BungeeManager.DELIMITER + world;
    }

    @Override
    public void process() {

        MultiWorldPlayer multiWorldPlayer = plugin.getPlayerManager().getPlayer(player);
        if (multiWorldPlayer == null) {
            return;
        }
        ServerLocation location = new ServerLocation(world, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z),
                Float.parseFloat(yaw), Float.parseFloat(pitch));
        multiWorldPlayer.setBeforeTeleportLocation(location);
    }
}
