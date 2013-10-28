package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.events.FoundPlayersServerEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author Philip
 */
@MessageName("SEND_PLAYERS_SERVER_MESSAGE")
public class SendPlayersServerMessage extends BungeeMessage {

    private String wanted;
    private String server;
    private String x;
    private String y;
    private String z;
    private String yaw;
    private String pitch;
    private String world;
    private RCMultiWorldPlugin plugin;

    public SendPlayersServerMessage(String wanted, String server, String x, String y, String z, String yaw, String pitch, String world) {

        this.wanted = wanted;
        this.server = server;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    public SendPlayersServerMessage(String wanted, String server, Location location) {

        this.wanted = wanted;
        this.server = server;
        this.x = String.valueOf(location.getX());
        this.y = String.valueOf(location.getY());
        this.z = String.valueOf(location.getZ());
        this.yaw = String.valueOf(location.getYaw());
        this.pitch = String.valueOf(location.getPitch());
        this.world = location.getWorld().getName();
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    @Override
    protected String encode() {

        return wanted + BungeeManager.DELIMITER + server + BungeeManager.DELIMITER + y + BungeeManager.DELIMITER + z + BungeeManager.DELIMITER
                + yaw + BungeeManager.DELIMITER + pitch + BungeeManager.DELIMITER + world;
    }

    @Override
    public void process() {

        Bukkit.getPluginManager().callEvent(new FoundPlayersServerEvent(wanted, server, world, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z),
                Float.parseFloat(yaw), Float.parseFloat(pitch)));
    }
}
