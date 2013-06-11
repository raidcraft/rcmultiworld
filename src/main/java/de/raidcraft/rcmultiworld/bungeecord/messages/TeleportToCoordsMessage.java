package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.listener.PlayerListener;
import de.raidcraft.rcmultiworld.tables.WorldInfoTable;
import de.raidcraft.rcmultiworld.utilclasses.LocatableLocation;
import de.raidcraft.rcmultiworld.utilclasses.ServerLocation;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
@MessageName("TELEPORT_TO_COORDS_MESSAGE")
public class TeleportToCoordsMessage extends BungeeMessage {

    private String player;
    private String x;
    private String y;
    private String z;
    private String yaw;
    private String pitch;
    private String world;
    private RCMultiWorldPlugin plugin;

    public TeleportToCoordsMessage(String player, String x, String y, String z, String yaw, String pitch, String world) {

        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    public TeleportToCoordsMessage(String player, Location location) {

        this.player = player;
        this.x = String.valueOf(location.getX());
        this.y = String.valueOf(location.getY());
        this.z = String.valueOf(location.getZ());
        this.yaw = String.valueOf(location.getYaw());
        this.pitch = String.valueOf(location.getPitch());
        this.world = location.getWorld().getName();
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    public TeleportToCoordsMessage(String player, ServerLocation serverLocation) {

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

        Player teleportPlayer = Bukkit.getPlayer(player);

        for(World existingWorld : Bukkit.getWorlds()) {

            if(existingWorld.getName().equalsIgnoreCase(world)) {

                Location location = new Location(existingWorld, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z),
                        Float.parseFloat(yaw), Float.parseFloat(pitch));
                if(teleportPlayer != null) {
                    teleportPlayer.teleport(location);
                    teleportPlayer.sendMessage(ChatColor.YELLOW + "Teleported.");
                }
                else {
                    PlayerListener.enqueuePlayer(player, new LocatableLocation(location));
                }
                return;
            }
        }

        // if player is here but on the wrong server -> change server
        if(teleportPlayer != null) {
            String targetServer = RaidCraft.getTable(WorldInfoTable.class).getWorldHost(world);
            BungeeCordUtil.changeServer(teleportPlayer, targetServer);
        }
    }
}
