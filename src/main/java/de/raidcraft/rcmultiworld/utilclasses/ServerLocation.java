package de.raidcraft.rcmultiworld.utilclasses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Philip
 */
public class ServerLocation {

    private String server;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public ServerLocation(String server, double x, double y, double z, float yaw, float pitch) {

        this.server = server;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public ServerLocation(String server, double x, double y, double z) {

        this.server = server;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    public ServerLocation(Location location) {

        this.server = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Location getBukkitLocation() {

        for(World bukkitWorld : Bukkit.getWorlds()) {
            if(bukkitWorld.getName().equalsIgnoreCase(server)) {

                return new Location(bukkitWorld, x, y, z, yaw, pitch);
            }
        }
        return null;
    }

    public void setServer(String server) {

        this.server = server;
    }

    public void setX(double x) {

        this.x = x;
    }

    public void setY(double y) {

        this.y = y;
    }

    public void setZ(double z) {

        this.z = z;
    }

    public void setYaw(float yaw) {

        this.yaw = yaw;
    }

    public void setPitch(float pitch) {

        this.pitch = pitch;
    }

    public String getServer() {

        return server;
    }

    public double getX() {

        return x;
    }

    public double getY() {

        return y;
    }

    public double getZ() {

        return z;
    }

    public float getYaw() {

        return yaw;
    }

    public float getPitch() {

        return pitch;
    }
}
