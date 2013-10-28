package de.raidcraft.rcmultiworld.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FoundPlayersServerEvent extends Event {

    private String player;
    private String server;
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public FoundPlayersServerEvent(String player, String server, String world, double x, double y, double z, float yaw, float pitch) {

        this.player = player;
        this.server = server;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getPlayer() {

        return player;
    }

    public String getServer() {

        return server;
    }

    public String getWorld() {

        return world;
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

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }
}
