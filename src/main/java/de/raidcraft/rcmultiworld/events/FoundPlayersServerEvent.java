package de.raidcraft.rcmultiworld.events;

import lombok.Value;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Value
public class FoundPlayersServerEvent extends Event {

    private final String player;
    private final String server;
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public FoundPlayersServerEvent(final String player,
                                   final String server,
                                   final String world,
                                   final double x,
                                   final double y,
                                   final double z,
                                   final float yaw,
                                   final float pitch) {

        this.player = player;
        this.server = server;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {

        return HANDLERS;
    }

    public static HandlerList getHandlerList() {

        return HANDLERS;
    }
}
