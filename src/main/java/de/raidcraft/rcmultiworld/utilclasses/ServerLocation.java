package de.raidcraft.rcmultiworld.utilclasses;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Philip
 */
@Data
public class ServerLocation {

    private String server;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public ServerLocation(final String server, final double x, final double y, final double z, final float yaw, final float pitch) {

        this.server = server;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public ServerLocation(final String server, final double x, final double y, final double z) {

        this.server = server;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    public ServerLocation(final Location location) {

        this.server = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Location getBukkitLocation() {

        for(final World bukkitWorld : Bukkit.getWorlds()) {
            if(bukkitWorld.getName().equalsIgnoreCase(this.server)) {

                return new Location(bukkitWorld, this.x, this.y, this.z, this.yaw, this.pitch);
            }
        }
        return null;
    }

    public void setServer(final String server) {

        this.server = server;
    }
}
