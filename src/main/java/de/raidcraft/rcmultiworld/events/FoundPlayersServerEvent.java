package de.raidcraft.rcmultiworld.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FoundPlayersServerEvent extends Event {

    private String player;
    private String server;

    public FoundPlayersServerEvent(String player, String server) {

        this.player = player;
        this.server = server;
    }

    public String getPlayer() {

        return player;
    }

    public String getServer() {

        return server;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }
}
